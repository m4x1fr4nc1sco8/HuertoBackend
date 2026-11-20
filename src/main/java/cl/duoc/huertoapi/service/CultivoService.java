package cl.duoc.huertoapi.service;

import cl.duoc.huertoapi.dto.CultivoRequest;
import cl.duoc.huertoapi.dto.CultivoResponse;
import cl.duoc.huertoapi.dto.EnviarTareaRequest;
import cl.duoc.huertoapi.exception.ApiException;
import cl.duoc.huertoapi.model.Cultivo;
import cl.duoc.huertoapi.model.EstadoCultivo;
import cl.duoc.huertoapi.model.EstadoTarea;
import cl.duoc.huertoapi.model.Sector;
import cl.duoc.huertoapi.model.Usuario;
import cl.duoc.huertoapi.repository.CultivoRepository;
import cl.duoc.huertoapi.repository.EstadoCultivoRepository;
import cl.duoc.huertoapi.repository.EstadoTareaRepository;
import cl.duoc.huertoapi.repository.SectorRepository;
import cl.duoc.huertoapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CultivoService {

    private static final String PENDIENTE = "PENDIENTE";
    private static final String PENDIENTE_DE_VALIDACION = "PENDIENTE_DE_VALIDACION";
    private static final String VALIDADA = "VALIDADA";

    private final CultivoRepository cultivoRepository;
    private final SectorRepository sectorRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoCultivoRepository estadoCultivoRepository;
    private final EstadoTareaRepository estadoTareaRepository;

    // Un estudiante ve solo sus cultivos (estudianteId); el administrador ve todos
    @Transactional(readOnly = true)
    public List<CultivoResponse> listar(Long estudianteId, Long sectorId) {
        List<Cultivo> cultivos;
        if (estudianteId != null) {
            cultivos = cultivoRepository.findByEstudianteId(estudianteId);
        } else if (sectorId != null) {
            cultivos = cultivoRepository.findBySectorId(sectorId);
        } else {
            cultivos = cultivoRepository.findAll();
        }
        return cultivos.stream().map(CultivoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CultivoResponse obtener(Long id) {
        return CultivoResponse.from(buscar(id));
    }

    public CultivoResponse crear(CultivoRequest req) {
        Cultivo cultivo = new Cultivo();
        cultivo.setEstadoTarea(estadoTarea(PENDIENTE));
        aplicar(cultivo, req);
        return CultivoResponse.from(cultivoRepository.save(cultivo));
    }

    public CultivoResponse actualizar(Long id, CultivoRequest req) {
        Cultivo cultivo = buscar(id);
        aplicar(cultivo, req);
        return CultivoResponse.from(cultivoRepository.save(cultivo));
    }

    public void eliminar(Long id) {
        cultivoRepository.delete(buscar(id));
    }

    public CultivoResponse cambiarEstado(Long id, String estado) {
        Cultivo cultivo = buscar(id);
        cultivo.setEstado(estadoCultivo(estado));
        return CultivoResponse.from(cultivoRepository.save(cultivo));
    }

    // ---------- flujo de la tarea ----------

    // Estudiante: envía la tarea con foto y comentario
    public CultivoResponse enviarTarea(Long id, EnviarTareaRequest req) {
        Cultivo cultivo = buscar(id);
        if (cultivo.getEstudiante() == null
                || !cultivo.getEstudiante().getId().equals(req.estudianteId())) {
            throw ApiException.prohibido("El cultivo no está asignado a este estudiante");
        }
        String actual = cultivo.getEstadoTarea().getNombre();
        if (PENDIENTE_DE_VALIDACION.equals(actual) || VALIDADA.equals(actual)) {
            throw ApiException.conflicto("La tarea ya fue enviada o validada");
        }
        cultivo.setEstadoTarea(estadoTarea(PENDIENTE_DE_VALIDACION));
        cultivo.setFotoUrl(req.fotoUrl());
        cultivo.setComentarioEstudiante(req.comentario().trim());
        cultivo.setMotivoRechazo(null);
        return CultivoResponse.from(cultivoRepository.save(cultivo));
    }

    // Administrador: valida la tarea enviada
    public CultivoResponse validarTarea(Long id) {
        Cultivo cultivo = buscar(id);
        exigirPendienteDeValidacion(cultivo);
        cultivo.setEstadoTarea(estadoTarea(VALIDADA));
        cultivo.setMotivoRechazo(null);
        return CultivoResponse.from(cultivoRepository.save(cultivo));
    }

    // Administrador: rechaza con motivo; la tarea vuelve a pendiente para que el estudiante reenvíe
    public CultivoResponse rechazarTarea(Long id, String motivo) {
        Cultivo cultivo = buscar(id);
        exigirPendienteDeValidacion(cultivo);
        cultivo.setEstadoTarea(estadoTarea(PENDIENTE));
        cultivo.setMotivoRechazo(motivo.trim());
        return CultivoResponse.from(cultivoRepository.save(cultivo));
    }

    private void exigirPendienteDeValidacion(Cultivo cultivo) {
        if (!PENDIENTE_DE_VALIDACION.equals(cultivo.getEstadoTarea().getNombre())) {
            throw ApiException.conflicto("La tarea no está pendiente de validación");
        }
    }

    // ---------- apoyo ----------

    private void aplicar(Cultivo cultivo, CultivoRequest req) {
        if (!req.fechaCosecha().isAfter(req.fechaSiembra())) {
            throw ApiException.solicitudInvalida("La cosecha debe ser posterior a la siembra");
        }
        Sector sector = sectorRepository.findById(req.sectorId())
                .orElseThrow(() -> ApiException.solicitudInvalida("El sector indicado no existe"));

        Usuario estudiante = null;
        if (req.estudianteId() != null) {
            estudiante = usuarioRepository.findById(req.estudianteId())
                    .orElseThrow(() -> ApiException.solicitudInvalida("El estudiante indicado no existe"));
            if (!"ESTUDIANTE".equals(estudiante.getRol().getNombre())) {
                throw ApiException.solicitudInvalida("El usuario indicado no es estudiante");
            }
        }

        cultivo.setNombre(req.nombre().trim());
        cultivo.setSector(sector);
        cultivo.setEstudiante(estudiante);
        cultivo.setFechaSiembra(req.fechaSiembra());
        cultivo.setFechaCosecha(req.fechaCosecha());
        cultivo.setFechaLimite(req.fechaLimite());
        cultivo.setFrecuenciaRiegoDias(req.frecuenciaRiegoDias());
        if (req.estado() != null && !req.estado().isBlank()) {
            cultivo.setEstado(estadoCultivo(req.estado()));
        } else if (cultivo.getEstado() == null) {
            cultivo.setEstado(estadoCultivo("PLANIFICADO"));
        }
    }

    private Cultivo buscar(Long id) {
        return cultivoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado("No se encontró el cultivo " + id));
    }

    private EstadoCultivo estadoCultivo(String nombre) {
        String buscado = nombre.trim();
        return estadoCultivoRepository.findByNombre(buscado)
                .orElseThrow(() -> ApiException.solicitudInvalida("Estado de cultivo inválido: " + buscado));
    }

    private EstadoTarea estadoTarea(String nombre) {
        return estadoTareaRepository.findByNombre(nombre)
                .orElseThrow(() -> ApiException.solicitudInvalida("Estado de tarea inválido: " + nombre));
    }
}
