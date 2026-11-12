package mx.fes.unam.model.entities.bitacora;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BitacoraDTO {
    private Long id;
    private Long adminId;
    private String adminNombre;
    private String entidad;
    private Long entidadId;
    private Accion accion;
    private String detalle;
    private LocalDateTime fecha;
}