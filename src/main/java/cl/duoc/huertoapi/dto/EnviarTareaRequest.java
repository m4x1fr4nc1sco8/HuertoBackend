package cl.duoc.huertoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EnviarTareaRequest(
        @NotNull Long estudianteId,
        @NotBlank @Size(max = 500) String comentario,
        @Size(max = 255) String fotoUrl
) {
}
