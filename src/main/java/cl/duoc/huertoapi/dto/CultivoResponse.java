package cl.duoc.huertoapi.dto;

import cl.duoc.huertoapi.model.Cultivo;

import java.time.LocalDate;

public record CultivoResponse(
        Long id,
        String nombre,
        Long sectorId,
        Long estudianteId,
        LocalDate fechaSiembra,
        LocalDate fechaCosecha,
        LocalDate fechaLimite,
        Integer frecuenciaRiegoDias,
        String estado,
        String estadoTarea,
        String fotoUrl,
        String comentarioEstudiante,
        String motivoRechazo
) {
    public static CultivoResponse from(Cultivo cultivo) {
        return new CultivoResponse(
                cultivo.getId(),
                cultivo.getNombre(),
                cultivo.getSector().getId(),
                cultivo.getEstudiante() == null ? null : cultivo.getEstudiante().getId(),
                cultivo.getFechaSiembra(),
                cultivo.getFechaCosecha(),
                cultivo.getFechaLimite(),
                cultivo.getFrecuenciaRiegoDias(),
                cultivo.getEstado().getNombre(),
                cultivo.getEstadoTarea().getNombre(),
                cultivo.getFotoUrl(),
                cultivo.getComentarioEstudiante(),
                cultivo.getMotivoRechazo()
        );
    }
}
