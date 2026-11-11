package mx.fes.unam.service.producto;

import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.repository.ProductoRepository;

import java.util.List;

public class ProductoService {
    private final ProductoMapper productoMapper;
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoMapper productoMapper, ProductoRepository productoRepository){
        this.productoMapper = productoMapper;
        this.productoRepository = productoRepository;
    }

    public List<ProductoDTO> listarProductos(){
        return productoRepository.findAll()
                .stream()
                .map(pr -> productoMapper.toDTO(pr) )
                .toList();
    }

    public ProductoDTO obtenerProducto(Long id){return productoMapper.toDTO(productoRepository.findById(id).orElse(null));}

    public ProductoDTO guardarProducto(ProductoDTO producto){
        ProductoEntity pr = productoRepository.save(productoMapper.toEntity(producto));
        producto = productoMapper.toDTO(pr);
        return producto;
    }

    public void eliminarLibro(Long id){productoRepository.deleteById(id);}

}
