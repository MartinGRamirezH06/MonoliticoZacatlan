package mx.fes.unam.model.entities.venta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;

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
    private VendedorEntity vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id",referencedColumnName = "id", nullable = false)
    private ProductoEntity producto;

    @Column(nullable = false, length = 50)
    private DateFormat fecha;

    @Column(nullable = false, length = 50)
    private Long cantidad;

    @Column(nullable = false,length = 50)
    private Double total;
}
