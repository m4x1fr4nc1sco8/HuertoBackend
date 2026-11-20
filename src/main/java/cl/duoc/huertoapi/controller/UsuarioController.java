package cl.duoc.huertoapi.controller;

import cl.duoc.huertoapi.dto.UsuarioRequest;
import cl.duoc.huertoapi.dto.UsuarioResponse;
import cl.duoc.huertoapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Registro y consulta de usuarios (los cultivos se asignan a estudiantes)")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios; filtro opcional rol=ESTUDIANTE o ADMINISTRADOR")
    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) String rol) {
        return usuarioService.listar(rol);
    }

    @Operation(summary = "Obtener un usuario por id (404 si no existe)")
    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Long id) {
        return usuarioService.obtener(id);
    }

    @Operation(summary = "Registrar un usuario (409 si el correo ya existe)")
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(req));
    }
}
