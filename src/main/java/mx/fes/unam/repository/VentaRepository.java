package mx.fes.unam.repository;

import mx.fes.unam.model.entities.venta.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository <VentaEntity,Long> {
    List<VentaEntity> findByVendedorNombreContainingIgnoreCase( String vendedorNombre);
    List<VentaEntity> findByProductoNombreContainingIgnoreCase( String productoNombre);
    List<VentaEntity> findByFecha(DateFormat fecha);
    List<VentaEntity> findByTotalContainingIgnoreCase(Long total);

    @Query("""
       SELECT v FROM VentaEntity v
       JOIN FETCH v.vendedor vd
       JOIN FETCH vd.bloque b
       JOIN FETCH v.producto p
       WHERE v.status = true
         AND v.fecha BETWEEN :desde AND :hasta
         AND (:vendedorId IS NULL OR vd.id = :vendedorId)
         AND (:bloqueId   IS NULL OR b.id  = :bloqueId)
         AND (:productoId IS NULL OR p.id  = :productoId)
       ORDER BY v.fecha, vd.apellido_paterno, v.id
       """)
    List<VentaEntity> buscarParaReporte(@Param("desde") LocalDate desde,
                                        @Param("hasta") LocalDate hasta,
                                        @Param("vendedorId") Long vendedorId,
                                        @Param("bloqueId") Long bloqueId,
                                        @Param("productoId") Long productoId);
}
