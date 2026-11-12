package mx.fes.unam.repository;

import mx.fes.unam.model.entities.bloque.BloqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloqueRepository extends JpaRepository <BloqueEntity,Long>{
    List<BloqueEntity> findByStatusTrue();
    List<BloqueEntity> findByNombreContainingIgnoreCase(String nombre);
    Optional<BloqueEntity> findByNombreIgnoreCaseAndStatusTrue(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
