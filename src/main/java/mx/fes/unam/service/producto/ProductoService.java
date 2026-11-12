package mx.fes.unam.service.producto;

import jakarta.persistence.EntityNotFoundException;
import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bloque.BloqueDTO;
import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.repository.ProductoRepository;
import mx.fes.unam.service.bitacora.BitacoraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {
    private final ProductoMapper productoMapper;
    private final ProductoRepository productoRepository;
    private final BitacoraService bitacoraService;

    public ProductoService(ProductoMapper productoMapper, ProductoRepository productoRepository,
                           BitacoraService bitacoraService) {
        this.productoMapper = productoMapper;
        this.productoRepository = productoRepository;
        this.bitacoraService = bitacoraService;
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProductos(boolean activos) {
        List<ProductoEntity> lista = activos ? productoRepository.findByStatusTrue()
                : productoRepository.findAll();
        return lista.stream().map(productoMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerProducto(Long id) {
        return productoMapper.toDTO(productoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado: " + id)));
    }


    public ProductoDTO crearProducto(ProductoDTO producto, Long adminId) {
        ProductoEntity pd = productoRepository.save(productoMapper.toEntity(producto));
        bitacoraService.registrar(adminId, "PRODUCTO", pd.getId(), Accion.CREAR,
                "Nombre: " + pd.getNombre() + ", Precio mín: " + pd.getPrecio_min());
        return productoMapper.toDTO(pd);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto, Long adminId) {
        ProductoEntity pd = productoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado: " + id));
        ;
        String antes = "Nombre: " + pd.getNombre() + ", Precio mín: " + pd.getPrecio_min()
                + ", Categoría: " + pd.getCategoria();

        pd.setNombre(dto.getNombre());
        pd.setPrecio_min(dto.getPrecio_min());
        pd.setCategoria(dto.getCategoria());

        bitacoraService.registrar(adminId, "PRODUCTO", id, Accion.EDITAR,
                "Antes: " + antes + " | Después: Nombre: " + dto.getNombre()
                        + ", Precio mín: " + dto.getPrecio_min() + ", Categoría: " + dto.getCategoria());
        return productoMapper.toDTO(pd);
    }

    public void desactivarProducto(Long id, Long adminId) {
        ProductoEntity pd = productoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado: " + id));
        pd.setStatus(false);
        bitacoraService.registrar(adminId, "PRODUCTO", id, Accion.DESACTIVAR, null);
    }

    public void reactivarProducto(Long id, Long adminId) {
        ProductoEntity pd = productoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado: " + id));
        pd.setStatus(true);
        bitacoraService.registrar(adminId, "PRODUCTO", id, Accion.REACTIVAR, null);
    }
}
