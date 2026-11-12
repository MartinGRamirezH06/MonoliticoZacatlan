package mx.fes.unam.repository;

import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendedorRepository extends JpaRepository <VendedorEntity,Long>{
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, Long id);
    List<VendedorEntity> findByStatusTrue();
    boolean existsByBloqueIdAndStatusTrue(Long bloqueId);
}
