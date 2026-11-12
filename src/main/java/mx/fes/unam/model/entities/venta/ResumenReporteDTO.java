package mx.fes.unam.model.entities.venta;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ResumenReporteDTO {
    private String etiqueta;
    private long numeroVentas;
    private BigDecimal total;
}
