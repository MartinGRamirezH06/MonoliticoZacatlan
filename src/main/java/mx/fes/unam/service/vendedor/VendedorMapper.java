package mx.fes.unam.service.vendedor;

import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;

public class VendedorMapper {
    public VendedorEntity toEntity(VendedorDTO dto){
        VendedorEntity vendedor = new VendedorEntity();
        if(dto!=null){
            vendedor.setId(dto.getId());
            vendedor.setNombre(dto.getNombre());
            vendedor.setApellido_paterno(dto.getApellido_paterno());
            vendedor.setApellido_materno(dto.getApellido_materno());
            vendedor.setCorreo(dto.getCorreo());
            vendedor.setTelefono(dto.getTelefono());
            vendedor.setRol(dto.getRol());
            vendedor.setStatus(dto.getStatus());
            vendedor.setPassword(dto.getPassword());

            if(dto.getBloqueId() != null){
                BloqueEntity bloque = new BloqueEntity();
                bloque.setId(dto.getBloqueId());
                vendedor.setBloque(bloque);
            }
        }
        return vendedor;
    }

    public VendedorDTO toDTO(VendedorEntity vendedor){
        VendedorDTO dto = null;

        Long bloqueId = null;
        String nombreBloque = null;

        if(vendedor.getBloque() != null){
            bloqueId = vendedor.getBloque().getId();
            nombreBloque = vendedor.getBloque().getNombre();
        }

        if(vendedor != null){
            dto = new VendedorDTO(
                    vendedor.getId(),
                    bloqueId,
                    nombreBloque,
                    vendedor.getNombre(),
                    vendedor.getApellido_paterno(),
                    vendedor.getApellido_materno(),
                    vendedor.getCorreo(),
                    vendedor.getTelefono(),
                    vendedor.getRol(),
                    vendedor.getStatus(),
                    vendedor.getPassword()
            );
        }
        return dto;
    }
}
