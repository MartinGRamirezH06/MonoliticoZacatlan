package mx.fes.unam.repository;


import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository <ProductoEntity,Long>{
    List<ProductoEntity> findByStatusTrue();
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoEntity> findByCategoriaContainingIgnoreCase(String categoria);
    List<ProductoEntity> findByCategoriaIgnoreCaseAndStatusTrue(String categoria);
    boolean existsByCategoriaAndStatusTrue(String categoria);

    @Modifying
    @Query("UPDATE ProductoEntity p SET p.categoria = :nueva WHERE p.categoria = :anterior")
    int renombrarCategoria(@Param("anterior") String anterior, @Param("nueva") String nueva);
}