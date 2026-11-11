package mx.fes.unam.repository;

import mx.fes.unam.model.entities.Producto.ProductoEntity;
import mx.fes.unam.model.entities.Vendedor.VendedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendedorRepository extends JpaRepository <VendedorEntity,Long>{
    List<VendedorEntity> findByNombreContainingIgnoreCase(String nombre);
    List<VendedorEntity> findByCorreoContainingIgnoreCase(String correo);
    List<VendedorEntity> findByTelefonoContainingIgnoreCase(String telefono);
    List<VendedorEntity> findByRolContainingIgnoreCase(String rol);
}
