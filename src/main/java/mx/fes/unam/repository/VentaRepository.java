package mx.fes.unam.repository;

import mx.fes.unam.model.entities.venta.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.DateFormat;
import java.util.List;

public interface VentaRepository extends JpaRepository <VentaEntity,Long> {
    List<VentaEntity> findByVendedorNombreContainingIgnoreCase( String vendedorNombre);
    List<VentaEntity> findByProductoNombreContainingIgnoreCase( String productoNombre);
    List<VentaEntity> findByFecha(DateFormat fecha);
    List<VentaEntity> findByTotalContainingIgnoreCase(Long total);
}
