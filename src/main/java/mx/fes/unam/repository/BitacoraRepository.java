package mx.fes.unam.repository;

import mx.fes.unam.model.entities.bitacora.BitacoraEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<BitacoraEntity, Long> {

    Page<BitacoraEntity> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("""
       SELECT b FROM BitacoraEntity b
       JOIN FETCH b.admin a
       WHERE (:entidad IS NULL OR b.entidad = :entidad)
         AND (:adminId IS NULL OR a.id = :adminId)
         AND b.fecha >= :desde AND b.fecha < :hasta
       ORDER BY b.fecha DESC, b.id DESC
       """)
    List<BitacoraEntity> buscar(@Param("entidad") String entidad,
                                @Param("adminId") Long adminId,
                                @Param("desde") LocalDateTime desde,
                                @Param("hasta") LocalDateTime hasta);

    List<BitacoraEntity> findByEntidadAndEntidadIdOrderByFechaDesc(String entidad, Long entidadId);

    List<BitacoraEntity> findByAdminIdOrderByFechaDesc(Long adminId);

    List<BitacoraEntity> findByFechaGreaterThanEqualAndFechaLessThanOrderByFechaDesc(
            LocalDateTime desde, LocalDateTime hasta);
}