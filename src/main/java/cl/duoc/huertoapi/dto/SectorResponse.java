package cl.duoc.huertoapi.dto;

import cl.duoc.huertoapi.model.Sector;

import java.time.LocalDate;

public record SectorResponse(
        Long id,
        String codigo,
        String nombre,
        String ubicacion,
        String estado,
        LocalDate fechaCreacion,
        String fotoUrl
) {
    public static SectorResponse from(Sector sector) {
        return new SectorResponse(
                sector.getId(),
                sector.getCodigo(),
                sector.getNombre(),
                sector.getUbicacion(),
                sector.getEstado().getNombre(),
                sector.getFechaCreacion(),
                sector.getFotoUrl()
        );
    }
}
