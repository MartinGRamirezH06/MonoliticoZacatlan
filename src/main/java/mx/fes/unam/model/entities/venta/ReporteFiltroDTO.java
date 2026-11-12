package mx.fes.unam.model.entities.venta;

import lombok.Data;
import mx.fes.unam.service.venta.AgruparPor;
import mx.fes.unam.service.venta.Periodo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ReporteFiltroDTO {
    private Periodo periodo = Periodo.DIA;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate desde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate hasta;

    private Long vendedorId;
    private Long bloqueId;
    private Long productoId;
    private AgruparPor agruparPor = AgruparPor.NINGUNO;
}
