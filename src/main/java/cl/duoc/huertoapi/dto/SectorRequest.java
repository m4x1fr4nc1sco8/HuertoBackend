package cl.duoc.huertoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SectorRequest(
        @NotBlank @Size(max = 20) String codigo,
        @NotBlank @Size(min = 3, max = 60) String nombre,
        @NotBlank @Size(max = 100) String ubicacion,
        // DISPONIBLE, EN_USO, EN_MANTENIMIENTO o FUERA_DE_USO (por defecto DISPONIBLE)
        String estado,
        @Size(max = 255) String fotoUrl
) {
}
