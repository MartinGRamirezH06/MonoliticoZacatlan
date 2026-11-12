package mx.fes.unam.model.entities.producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 50)
    private String nombre;

    @Column(nullable = true)
    private BigDecimal precio_min;

    @Column(nullable = true, length = 50)
    private String categoria;

    @Column(nullable = false)
    private Boolean status;

    //@Column(name = "ruta_imagen", length = 255)
    //private String rutaImagen;

    //Me falta verificar si necesito crear una columna
    // para guardar la imagen en la base

}
