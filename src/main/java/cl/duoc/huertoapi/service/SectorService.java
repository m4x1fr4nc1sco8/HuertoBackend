package cl.duoc.huertoapi.service;

import cl.duoc.huertoapi.dto.SectorRequest;
import cl.duoc.huertoapi.dto.SectorResponse;
import cl.duoc.huertoapi.exception.ApiException;
import cl.duoc.huertoapi.model.EstadoSector;
import cl.duoc.huertoapi.model.Sector;
import cl.duoc.huertoapi.repository.CultivoRepository;
import cl.duoc.huertoapi.repository.EstadoSectorRepository;
import cl.duoc.huertoapi.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SectorService {

    private final SectorRepository sectorRepository;
    private final EstadoSectorRepository estadoSectorRepository;
    private final CultivoRepository cultivoRepository;

    @Transactional(readOnly = true)
    public List<SectorResponse> listar() {
        return sectorRepository.findAll().stream().map(SectorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SectorResponse obtener(Long id) {
        return SectorResponse.from(buscar(id));
    }

    public SectorResponse crear(SectorRequest req) {
        String codigo = req.codigo().trim();
        if (sectorRepository.existsByCodigo(codigo)) {
            throw ApiException.conflicto("Ya existe un sector con ese código");
        }
        Sector sector = new Sector();
        sector.setFechaCreacion(LocalDate.now());
        sector.setEstado(estado(req.estado(), "DISPONIBLE"));
        aplicar(sector, req, codigo);
        return SectorResponse.from(sectorRepository.save(sector));
    }

    public SectorResponse actualizar(Long id, SectorRequest req) {
        Sector sector = buscar(id);
        String codigo = req.codigo().trim();
        if (sectorRepository.existsByCodigoAndIdNot(codigo, id)) {
            throw ApiException.conflicto("Ya existe un sector con ese código");
        }
        // Si no se indica estado, se conserva el actual
        if (req.estado() != null && !req.estado().isBlank()) {
            sector.setEstado(estado(req.estado(), sector.getEstado().getNombre()));
        }
        aplicar(sector, req, codigo);
        return SectorResponse.from(sectorRepository.save(sector));
    }

    public void eliminar(Long id) {
        Sector sector = buscar(id);
        if (cultivoRepository.existsBySectorId(id)) {
            throw ApiException.conflicto("No se puede eliminar: el sector tiene cultivos asociados");
        }
        sectorRepository.delete(sector);
    }

    private void aplicar(Sector sector, SectorRequest req, String codigo) {
        sector.setCodigo(codigo);
        sector.setNombre(req.nombre().trim());
        sector.setUbicacion(req.ubicacion().trim());
        sector.setFotoUrl(req.fotoUrl());
    }

    private Sector buscar(Long id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado("No se encontró el sector " + id));
    }

    private EstadoSector estado(String nombre, String porDefecto) {
        String buscado = (nombre == null || nombre.isBlank()) ? porDefecto : nombre.trim();
        return estadoSectorRepository.findByNombre(buscado)
                .orElseThrow(() -> ApiException.solicitudInvalida("Estado de sector inválido: " + buscado));
    }
}
