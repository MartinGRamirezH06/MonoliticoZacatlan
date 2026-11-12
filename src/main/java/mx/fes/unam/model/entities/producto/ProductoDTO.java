package mx.fes.unam.model.entities.producto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    Long id;
    String nombre;
    BigDecimal precio_min;
    String categoria;
    Boolean status;

}
