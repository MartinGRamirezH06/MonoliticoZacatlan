package mx.fes.unam.model.entities.bitacora;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BitacoraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private VendedorEntity admin;

    @Column(nullable = false, length = 50)
    private String entidad;

    @Column(nullable = false)
    private Long entidadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Accion accion;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
