package cl.duoc.huertoapi.controller;

import cl.duoc.huertoapi.dto.CultivoRequest;
import cl.duoc.huertoapi.dto.CultivoResponse;
import cl.duoc.huertoapi.dto.EnviarTareaRequest;
import cl.duoc.huertoapi.dto.EstadoRequest;
import cl.duoc.huertoapi.dto.RechazarTareaRequest;
import cl.duoc.huertoapi.service.CultivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cultivos")
@Tag(name = "Cultivos", description = "CRUD de cultivos y flujo de la tarea (enviar, validar, rechazar)")
@RequiredArgsConstructor
public class CultivoController {

    private final CultivoService cultivoService;

    @Operation(summary = "Listar cultivos; filtros opcionales: estudianteId o sectorId")
    @GetMapping
    public List<CultivoResponse> listar(
            @RequestParam(required = false) Long estudianteId,
            @RequestParam(required = false) Long sectorId) {
        return cultivoService.listar(estudianteId, sectorId);
    }

    @Operation(summary = "Obtener un cultivo por id (404 si no existe)")
    @GetMapping("/{id}")
    public CultivoResponse obtener(@PathVariable Long id) {
        return cultivoService.obtener(id);
    }

    @Operation(summary = "Crear un cultivo (la tarea parte en PENDIENTE)")
    @PostMapping
    public ResponseEntity<CultivoResponse> crear(@Valid @RequestBody CultivoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cultivoService.crear(req));
    }

    @Operation(summary = "Actualizar los datos de un cultivo")
    @PutMapping("/{id}")
    public CultivoResponse actualizar(@PathVariable Long id, @Valid @RequestBody CultivoRequest req) {
        return cultivoService.actualizar(id, req);
    }

    @Operation(summary = "Eliminar un cultivo")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cultivoService.eliminar(id);
    }

    @Operation(summary = "Cambiar el estado de desarrollo del cultivo (administrador)")
    @PatchMapping("/{id}/estado")
    public CultivoResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest req) {
        return cultivoService.cambiarEstado(id, req.estado());
    }

    @Operation(summary = "Estudiante: enviar la tarea con foto y comentario (queda PENDIENTE_DE_VALIDACION)")
    @PostMapping("/{id}/tarea/enviar")
    public CultivoResponse enviarTarea(@PathVariable Long id, @Valid @RequestBody EnviarTareaRequest req) {
        return cultivoService.enviarTarea(id, req);
    }

    @Operation(summary = "Administrador: validar la tarea enviada (VALIDADA)")
    @PostMapping("/{id}/tarea/validar")
    public CultivoResponse validarTarea(@PathVariable Long id) {
        return cultivoService.validarTarea(id);
    }

    @Operation(summary = "Administrador: rechazar la tarea con un motivo (vuelve a PENDIENTE)")
    @PostMapping("/{id}/tarea/rechazar")
    public CultivoResponse rechazarTarea(@PathVariable Long id, @Valid @RequestBody RechazarTareaRequest req) {
        return cultivoService.rechazarTarea(id, req.motivo());
    }
}
