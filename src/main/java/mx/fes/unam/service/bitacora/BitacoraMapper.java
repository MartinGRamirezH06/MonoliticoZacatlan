package mx.fes.unam.service.bitacora;

import mx.fes.unam.model.entities.bitacora.BitacoraDTO;
import mx.fes.unam.model.entities.bitacora.BitacoraEntity;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import org.springframework.stereotype.Component;

@Component
public class BitacoraMapper {

    public BitacoraDTO toDTO(BitacoraEntity bitacora) {
        BitacoraDTO dto = null;
        if (bitacora != null) {
            VendedorEntity admin = bitacora.getAdmin();
            dto = new BitacoraDTO(
                    bitacora.getId(),
                    admin != null ? admin.getId() : null,
                    admin != null ? admin.getNombre() + " " + admin.getApellido_paterno() : null,
                    bitacora.getEntidad(),
                    bitacora.getEntidadId(),
                    bitacora.getAccion(),
                    bitacora.getDetalle(),
                    bitacora.getFecha()
            );
        }
        return dto;
    }
}