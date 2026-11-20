package cl.duoc.huertoapi.repository;

import cl.duoc.huertoapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreo(String correo);

    List<Usuario> findByRolNombre(String nombre);
}
