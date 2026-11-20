package cl.duoc.huertoapi.repository;

import cl.duoc.huertoapi.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    boolean existsByCodigo(String codigo);

    // Para editar: el código puede repetirse solo con el propio sector
    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
