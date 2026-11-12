package mx.fes.unam.model.entities.venta;

import lombok.Builder;
import lombok.Data;
import mx.fes.unam.service.venta.AgruparPor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ReporteVentasDTO {
    private String titulo;
    private LocalDate desde;
    private LocalDate hasta;
    private String filtros;
    private AgruparPor agruparPor;
    private int numeroVentas;
    private BigDecimal totalGeneral;
    private List<VentaDTO> ventas;
    private List<ResumenReporteDTO> resumen;
}
