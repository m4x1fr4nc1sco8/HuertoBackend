package cl.duoc.huertoapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UsuarioRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String apellido,
        @NotBlank @Email @Size(max = 100) String correo,
        @NotNull LocalDate fechaNacimiento,
        @NotBlank @Size(min = 8, max = 100) String clave,
        // ADMINISTRADOR o ESTUDIANTE
        @NotBlank String rol
) {
}
