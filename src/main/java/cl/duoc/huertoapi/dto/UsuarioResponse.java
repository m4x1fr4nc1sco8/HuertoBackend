package cl.duoc.huertoapi.dto;

import cl.duoc.huertoapi.model.Usuario;

import java.time.LocalDate;

// La clave nunca se devuelve
public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String correo,
        LocalDate fechaNacimiento,
        String rol
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getFechaNacimiento(),
                usuario.getRol().getNombre()
        );
    }
}
