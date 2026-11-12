package mx.fes.unam.service.venta;

import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import mx.fes.unam.model.entities.venta.VentaDTO;
import mx.fes.unam.model.entities.venta.VentaEntity;
import org.springframework.stereotype.Component;

@Component
public class VentaMapper {
    public VentaEntity toEntity(VentaDTO dto){
        VentaEntity venta = new VentaEntity();

        if(dto!=null){
            venta.setPrecioUnitario(dto.getPrecioUnitario());
            venta.setCantidad(dto.getCantidad());

            if(dto.getVendedorId() != null){
                VendedorEntity vendedor = new VendedorEntity();
                vendedor.setId(dto.getVendedorId());
                venta.setVendedor(vendedor);
            }

            if(dto.getProductoId() != null){
                ProductoEntity producto = new ProductoEntity();
                producto.setId(dto.getProductoId());
                venta.setProducto(producto);
            }
        }
        return venta;
    }

    public VentaDTO toDTO(VentaEntity venta){
        VentaDTO dto = null;

        if(venta != null){
            Long vendedorId = null;
            String nombreVendedor = null;
            Long bloqueId = null;
            String nombreBloque = null;
            Long productoId = null;
            String nombreProducto = null;

            if(venta.getVendedor() != null){
                vendedorId = venta.getVendedor().getId();
                nombreVendedor = venta.getVendedor().getNombre() + " "
                        + venta.getVendedor().getApellido_paterno();

                if(venta.getVendedor().getBloque() != null){
                    bloqueId = venta.getVendedor().getBloque().getId();
                    nombreBloque = venta.getVendedor().getBloque().getNombre();
                }
            }

            if(venta.getProducto() != null){
                productoId = venta.getProducto().getId();
                nombreProducto = venta.getProducto().getNombre();
            }

            dto = new VentaDTO(
                    venta.getId(),
                    vendedorId,
                    nombreVendedor,
                    productoId,
                    nombreBloque,
                    bloqueId,
                    nombreProducto,
                    venta.getFecha(),
                    venta.getPrecioUnitario(),
                    venta.getCantidad(),
                    venta.getTotal()
            );
        }
        return dto;
    }
}