package cl.duoc.huertoapi.repository;

import cl.duoc.huertoapi.model.EstadoCultivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoCultivoRepository extends JpaRepository<EstadoCultivo, Integer> {

    Optional<EstadoCultivo> findByNombre(String nombre);
}
