package mx.fes.unam.model.entities.venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    LocalDate fecha;
    Long cantidad;
    Double total;
}
