package mx.fes.unam.service.producto;

import jakarta.persistence.EntityNotFoundException;
import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bloque.BloqueDTO;
import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.repository.BloqueRepository;
import mx.fes.unam.repository.ProductoRepository;
import mx.fes.unam.service.bitacora.BitacoraService;
import mx.fes.unam.util.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {
    private final ProductoMapper productoMapper;
    private final ProductoRepository productoRepository;
    private final BitacoraService bitacoraService;
    private final BloqueRepository bloqueRepository;

    public ProductoService(ProductoMapper productoMapper, ProductoRepository productoRepository,
                           BitacoraService bitacoraService, BloqueRepository bloqueRepository) {
        this.productoMapper = productoMapper;
        this.productoRepository = productoRepository;
        this.bitacoraService = bitacoraService;
        this.bloqueRepository = bloqueRepository;
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

    @Transactional(readOnly = true)
    public List<ProductoDTO> listarActivosPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) return List.of();
        return productoRepository.findByCategoriaIgnoreCaseAndStatusTrue(categoria.trim())
                .stream().map(productoMapper::toDTO).toList();
    }

    public ProductoDTO crearProducto(ProductoDTO producto, Long adminId) {
        ProductoEntity nuevo = productoMapper.toEntity(producto);
        nuevo.setCategoria(resolverCategoria(producto.getCategoria(), null));
        ProductoEntity pd = productoRepository.save(nuevo);
        bitacoraService.registrar(adminId, "PRODUCTO", pd.getId(), Accion.CREAR,
                "Nombre: " + pd.getNombre() + ", Precio mín: " + pd.getPrecio_min()
                        + ", Categoría: " + pd.getCategoria());
        return productoMapper.toDTO(pd);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto, Long adminId) {
        ProductoEntity pd = productoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado: " + id));
        ;
        String antes = "Nombre: " + pd.getNombre() + ", Precio mín: " + pd.getPrecio_min()
                + ", Categoría: " + pd.getCategoria();

        String categoria = resolverCategoria(dto.getCategoria(), pd.getCategoria());
        pd.setNombre(dto.getNombre());
        pd.setPrecio_min(dto.getPrecio_min());
        pd.setCategoria(categoria);

        bitacoraService.registrar(adminId, "PRODUCTO", id, Accion.EDITAR,
                "Antes: " + antes + " | Después: Nombre: " + dto.getNombre()
                        + ", Precio mín: " + dto.getPrecio_min() + ", Categoría: " + categoria);
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
        ReglaNegocioException.si(
                bloqueRepository.findByNombreIgnoreCaseAndStatusTrue(pd.getCategoria()).isEmpty(),
                "Su categoría (bloque) está inactiva; reactívala primero");
        pd.setStatus(true);
        bitacoraService.registrar(adminId, "PRODUCTO", id, Accion.REACTIVAR, null);
    }

    private String resolverCategoria(String categoria, String actual) {
        ReglaNegocioException.si(categoria == null || categoria.isBlank(), "Selecciona una categoría");
        String buscada = categoria.trim();
        if (actual != null && actual.equalsIgnoreCase(buscada)) {
            return actual;
        }
        return bloqueRepository.findByNombreIgnoreCaseAndStatusTrue(buscada)
                .orElseThrow(() -> new ReglaNegocioException("La categoría debe ser un bloque activo"))
                .getNombre();
    }
}