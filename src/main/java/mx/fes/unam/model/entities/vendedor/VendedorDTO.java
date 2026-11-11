package mx.fes.unam.model.entities.vendedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendedorDTO {
    Long id;
    Long bloqueId;
    String nombreBloque;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String correo;
    String telefono;
    String rol;
    Boolean status;
    String password;
}
