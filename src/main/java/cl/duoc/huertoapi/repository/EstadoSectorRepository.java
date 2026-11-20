package cl.duoc.huertoapi.repository;

import cl.duoc.huertoapi.model.EstadoSector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoSectorRepository extends JpaRepository<EstadoSector, Integer> {

    Optional<EstadoSector> findByNombre(String nombre);
}
