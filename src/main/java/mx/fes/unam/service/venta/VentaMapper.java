package mx.fes.unam.service.venta;

import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import mx.fes.unam.model.entities.venta.VentaDTO;
import mx.fes.unam.model.entities.venta.VentaEntity;

public class VentaMapper {
    public VentaEntity toEntity(VentaDTO dto){
        VentaEntity venta = new VentaEntity();

        if(dto!=null){
            venta.setId(dto.getId());
            venta.setFecha(dto.getFecha());
            venta.setCantidad(dto.getCantidad());
            venta.setTotal(dto.getTotal());

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

        Long vendedorId = null;
        String nombreVendedor = null;

        Long productoId = null;
        String nombreProducto = null;

        if(venta.getVendedor() != null){
            vendedorId = venta.getVendedor().getId();
            nombreVendedor = venta.getVendedor().getNombre();
        }

        if(venta.getProducto() != null){
            productoId = venta.getProducto().getId();
            nombreProducto = venta.getProducto().getNombre();
        }


        if(venta != null){
            dto = new VentaDTO(
                    venta.getId(),
                    vendedorId,
                    nombreVendedor,
                    productoId,
                    nombreProducto,
                    venta.getFecha(),
                    venta.getCantidad(),
                    venta.getTotal()
            );
        }
        return dto;
    }
}
