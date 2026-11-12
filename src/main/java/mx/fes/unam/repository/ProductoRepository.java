package mx.fes.unam.repository;


import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository <ProductoEntity,Long>{
    List<ProductoEntity> findByStatusTrue();
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoEntity> findByCategoriaContainingIgnoreCase(String categoria);
}
