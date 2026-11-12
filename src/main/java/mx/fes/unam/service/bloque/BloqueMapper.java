package mx.fes.unam.service.bloque;

import mx.fes.unam.model.entities.bloque.BloqueDTO;
import mx.fes.unam.model.entities.bloque.BloqueEntity;
import org.springframework.stereotype.Controller;

@Controller
public class BloqueMapper {
    public BloqueEntity toEntity(BloqueDTO dto){
        BloqueEntity bloque = new BloqueEntity();
        if(dto!=null){
            bloque.setId(dto.getId());
            bloque.setNombre(dto.getNombre());
            bloque.setDescripcion(dto.getDescripcion());
        }
        return bloque;
    }

    public BloqueDTO toDTO(BloqueEntity bloque){
        BloqueDTO dto = null;
        if(bloque != null){
            dto = new BloqueDTO(
                    bloque.getId(),
                    bloque.getNombre(),
                    bloque.getDescripcion()
            );
        }
        return dto;
    }
}
