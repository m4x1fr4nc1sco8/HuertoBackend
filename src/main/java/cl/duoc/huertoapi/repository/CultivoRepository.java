package cl.duoc.huertoapi.repository;

import cl.duoc.huertoapi.model.Cultivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CultivoRepository extends JpaRepository<Cultivo, Long> {

    List<Cultivo> findByEstudianteId(Long estudianteId);

    List<Cultivo> findBySectorId(Long sectorId);

    boolean existsBySectorId(Long sectorId);
}
