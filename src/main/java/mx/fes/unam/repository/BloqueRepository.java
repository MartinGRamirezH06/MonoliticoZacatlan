package mx.fes.unam.repository;

import mx.fes.unam.model.entities.bloque.BloqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloqueRepository extends JpaRepository <BloqueEntity,Long>{
    List<BloqueEntity> findByStatusTrue();
    List<BloqueEntity> findByNombreContainingIgnoreCase(String nombre);
}
