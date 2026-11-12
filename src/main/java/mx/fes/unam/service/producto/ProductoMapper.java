package mx.fes.unam.service.producto;


import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ProductoMapper {
    public ProductoEntity toEntity(ProductoDTO dto){
        ProductoEntity producto = new ProductoEntity();
        if(dto!=null){
            producto.setId(dto.getId());
            producto.setNombre(dto.getNombre());
            producto.setPrecio_min(dto.getPrecio_min());
            producto.setCategoria(dto.getCategoria());
        }
        return producto;
    }

    public ProductoDTO toDTO(ProductoEntity producto){
        ProductoDTO dto = null;
        if(producto != null){
            dto = new ProductoDTO(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio_min(),
                    producto.getCategoria()
            );
        }
        return dto;
    }
}
