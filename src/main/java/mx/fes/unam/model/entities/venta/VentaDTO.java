package mx.fes.unam.model.entities.venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaDTO {
    Long id;
    Long vendedorId;
    String vendedorNombre;
    Long productoId;
    String productoNombre;
    Long bloqueId;
    String bloqueNombre;
    LocalDate fecha;
    BigDecimal precioUnitario;
    Long cantidad;
    BigDecimal total;
}
