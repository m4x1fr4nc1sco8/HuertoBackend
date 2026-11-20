package cl.duoc.huertoapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CultivoRequest(
        @NotBlank @Size(min = 3, max = 60) String nombre,
        @NotNull Long sectorId,
        // opcional: id del estudiante al que se asigna el cultivo
        Long estudianteId,
        @NotNull LocalDate fechaSiembra,
        @NotNull LocalDate fechaCosecha,
        LocalDate fechaLimite,
        @NotNull @Min(1) @Max(30) Integer frecuenciaRiegoDias,
        // PLANIFICADO, SEMBRADO, EN_CRECIMIENTO, LISTO_PARA_COSECHAR, COSECHADO, CON_PROBLEMAS o FINALIZADO
        String estado
) {
}
