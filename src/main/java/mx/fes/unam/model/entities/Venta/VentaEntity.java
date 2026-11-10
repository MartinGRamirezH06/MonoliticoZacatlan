package mx.fes.unam.model.entities.Venta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.fes.unam.model.entities.Producto.ProductoEntity;

import java.text.DateFormat;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", referencedColumnName = "id", nullable = false)
    private VentaEntity vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id",referencedColumnName = "id", nullable = false)
    private ProductoEntity producto;

    @Column(nullable = false, length = 50)
    private DateFormat fecha_venta;

    @Column(nullable = false, length = 50)
    private Long cantidad;

    @Column(nullable = false,length = 50)
    private Double total;
}
