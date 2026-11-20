package cl.duoc.huertoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RechazarTareaRequest(@NotBlank @Size(max = 255) String motivo) {
}
