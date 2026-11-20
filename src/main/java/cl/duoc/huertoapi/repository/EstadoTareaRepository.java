package cl.duoc.huertoapi.repository;

import cl.duoc.huertoapi.model.EstadoTarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoTareaRepository extends JpaRepository<EstadoTarea, Integer> {

    Optional<EstadoTarea> findByNombre(String nombre);
}
