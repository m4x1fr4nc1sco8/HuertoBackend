package cl.duoc.huertoapi.controller;

import cl.duoc.huertoapi.dto.SectorRequest;
import cl.duoc.huertoapi.dto.SectorResponse;
import cl.duoc.huertoapi.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sectores")
@Tag(name = "Sectores", description = "CRUD de los sectores del huerto")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @Operation(summary = "Listar todos los sectores")
    @GetMapping
    public List<SectorResponse> listar() {
        return sectorService.listar();
    }

    @Operation(summary = "Obtener un sector por id (404 si no existe)")
    @GetMapping("/{id}")
    public SectorResponse obtener(@PathVariable Long id) {
        return sectorService.obtener(id);
    }

    @Operation(summary = "Crear un sector (409 si el código ya existe)")
    @PostMapping
    public ResponseEntity<SectorResponse> crear(@Valid @RequestBody SectorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectorService.crear(req));
    }

    @Operation(summary = "Actualizar un sector (404 si no existe, 409 si el código es de otro sector)")
    @PutMapping("/{id}")
    public SectorResponse actualizar(@PathVariable Long id, @Valid @RequestBody SectorRequest req) {
        return sectorService.actualizar(id, req);
    }

    @Operation(summary = "Eliminar un sector (409 si tiene cultivos asociados)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        sectorService.eliminar(id);
    }
}
