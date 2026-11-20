package cl.duoc.huertoapi.service;

import cl.duoc.huertoapi.dto.UsuarioRequest;
import cl.duoc.huertoapi.dto.UsuarioResponse;
import cl.duoc.huertoapi.exception.ApiException;
import cl.duoc.huertoapi.model.Rol;
import cl.duoc.huertoapi.model.Usuario;
import cl.duoc.huertoapi.repository.RolRepository;
import cl.duoc.huertoapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    // rol opcional: ADMINISTRADOR o ESTUDIANTE
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(String rol) {
        List<Usuario> usuarios = (rol == null || rol.isBlank())
                ? usuarioRepository.findAll()
                : usuarioRepository.findByRolNombre(rol.trim());
        return usuarios.stream().map(UsuarioResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponse::from)
                .orElseThrow(() -> ApiException.noEncontrado("No se encontró el usuario " + id));
    }

    public UsuarioResponse crear(UsuarioRequest req) {
        String correo = req.correo().trim().toLowerCase();
        if (usuarioRepository.existsByCorreo(correo)) {
            throw ApiException.conflicto("Ya existe un usuario con ese correo");
        }
        Rol rol = rolRepository.findByNombre(req.rol().trim())
                .orElseThrow(() -> ApiException.solicitudInvalida("Rol inválido: " + req.rol()));
        validarEdad(req.fechaNacimiento(), rol.getNombre());

        Usuario usuario = new Usuario();
        usuario.setNombre(req.nombre().trim());
        usuario.setApellido(req.apellido().trim());
        usuario.setCorreo(correo);
        usuario.setFechaNacimiento(req.fechaNacimiento());
        usuario.setClave(req.clave());
        usuario.setRol(rol);
        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    // Mismas reglas que el registro de la app
    private void validarEdad(LocalDate nacimiento, String rol) {
        LocalDate hoy = LocalDate.now();
        if (nacimiento.isAfter(hoy)) {
            throw ApiException.solicitudInvalida("La fecha no puede ser futura");
        }
        int edad = Period.between(nacimiento, hoy).getYears();
        if ("ADMINISTRADOR".equals(rol) && edad < 18) {
            throw ApiException.solicitudInvalida("El perfil administrador requiere ser mayor de 18 años");
        }
        if ("ESTUDIANTE".equals(rol) && edad < 4) {
            throw ApiException.solicitudInvalida("El perfil estudiante requiere tener al menos 4 años");
        }
    }
}
