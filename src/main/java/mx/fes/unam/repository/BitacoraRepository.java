package mx.fes.unam.repository;

import mx.fes.unam.model.entities.bitacora.BitacoraEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<BitacoraEntity, Long> {

    Page<BitacoraEntity> findAllByOrderByFechaDesc(Pageable pageable);

    List<BitacoraEntity> findByEntidadAndEntidadIdOrderByFechaDesc(String entidad, Long entidadId);

    List<BitacoraEntity> findByAdminIdOrderByFechaDesc(Long adminId);

    List<BitacoraEntity> findByFechaGreaterThanEqualAndFechaLessThanOrderByFechaDesc(
            LocalDateTime desde, LocalDateTime hasta);
}