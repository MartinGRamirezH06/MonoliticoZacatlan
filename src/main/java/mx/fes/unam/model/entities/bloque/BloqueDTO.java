package mx.fes.unam.model.entities.bloque;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloqueDTO {
    Long id;
    String nombre;
    String descripcion;

}
