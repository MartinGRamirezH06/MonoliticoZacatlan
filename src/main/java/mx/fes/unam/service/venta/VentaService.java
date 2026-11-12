package mx.fes.unam.service.venta;

import jakarta.persistence.EntityNotFoundException;
import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import mx.fes.unam.model.entities.venta.*;
import mx.fes.unam.repository.BloqueRepository;
import mx.fes.unam.repository.ProductoRepository;
import mx.fes.unam.repository.VendedorRepository;
import mx.fes.unam.repository.VentaRepository;
import mx.fes.unam.service.bitacora.BitacoraService;
import mx.fes.unam.service.vendedor.VendedorMapper;
import mx.fes.unam.util.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class VentaService {
    private static final ZoneId ZONA = ZoneId.of("America/Mexico_City");

    private final VentaMapper ventaMapper;
    private final VentaRepository ventaRepository;
    private final VendedorRepository vendedorRepository;
    private final ProductoRepository productoRepository;
    private final BloqueRepository bloqueRepository;
    private final BitacoraService bitacoraService;

    public VentaService(VentaMapper ventaMapper, VentaRepository ventaRepository,
                        VendedorRepository vendedorRepository, ProductoRepository productoRepository,
                        BitacoraService bitacoraService,
                        BloqueRepository bloqueRepository) {
        this.ventaMapper = ventaMapper;
        this.ventaRepository = ventaRepository;
        this.vendedorRepository = vendedorRepository;
        this.productoRepository = productoRepository;
        this.bitacoraService = bitacoraService;
        this.bloqueRepository = bloqueRepository;
    }

    public VentaDTO registrarVenta(VentaDTO dto, Long vendedorId) {
        VendedorEntity vendedor = vendedorRepository.findById(vendedorId)
                .filter(VendedorEntity::getStatus)
                .orElseThrow(() -> new ReglaNegocioException("Vendedor inexistente o inactivo"));
        ProductoEntity producto = buscarProductoActivo(dto.getProductoId());
        ReglaNegocioException.si(
                vendedor.getBloque() == null
                        || !vendedor.getBloque().getNombre().equalsIgnoreCase(producto.getCategoria()),
                "Ese producto no pertenece a tu bloque");

        BigDecimal precio = dto.getPrecioUnitario() != null
                ? dto.getPrecioUnitario()
                : producto.getPrecio_min();
        validarPrecio(precio, producto);

        VentaEntity venta = ventaMapper.toEntity(dto);
        venta.setVendedor(vendedor);
        venta.setProducto(producto);
        venta.setFecha(LocalDate.now(ZONA));
        venta.setPrecioUnitario(precio);
        venta.setTotal(calcularTotal(Math.toIntExact(dto.getCantidad()), precio));

        return ventaMapper.toDTO(ventaRepository.save(venta));
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> listarVentas() {
        return ventaRepository.findAll().stream().map(ventaMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VentaDTO obtenerVenta(Long id) {
        return ventaMapper.toDTO(buscar(id));
    }

    public VentaDTO actualizarVenta(Long id, VentaDTO dto, Long adminId) {
        VentaEntity venta = buscar(id);
        String antes = "Producto: " + venta.getProducto().getId() + ", Cantidad: " + venta.getCantidad()
                + ", Precio: " + venta.getPrecioUnitario() + ", Total: " + venta.getTotal();

        ProductoEntity producto = buscarProductoActivo(dto.getProductoId());
        validarPrecio(dto.getPrecioUnitario(), producto);

        venta.setProducto(producto);
        venta.setCantidad(dto.getCantidad());
        venta.setPrecioUnitario(dto.getPrecioUnitario());
        venta.setTotal(calcularTotal(Math.toIntExact(dto.getCantidad()), dto.getPrecioUnitario()));

        bitacoraService.registrar(adminId, "VENTA", id, Accion.EDITAR,
                "Antes: " + antes + " | Después: Producto: " + producto.getId()
                        + ", Cantidad: " + venta.getCantidad() + ", Precio: " + venta.getPrecioUnitario()
                        + ", Total: " + venta.getTotal());
        return ventaMapper.toDTO(venta);
    }

    public void desactivarVenta(Long id, Long adminId) {
        buscar(id).setStatus(false);
        bitacoraService.registrar(adminId, "VENTA", id, Accion.DESACTIVAR, null);
    }

    public void reactivarVenta(Long id, Long adminId) {
        buscar(id).setStatus(true);
        bitacoraService.registrar(adminId, "VENTA", id, Accion.REACTIVAR, null);
    }


    private BigDecimal calcularTotal(Integer cantidad, BigDecimal precioUnitario) {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
    }

    private void validarPrecio(BigDecimal precio, ProductoEntity producto) {
        if (producto.getPrecio_min() != null && precio.compareTo(producto.getPrecio_min()) < 0) {
            throw new ReglaNegocioException(
                    "El precio no puede ser menor al mínimo: " + producto.getPrecio_min());
        }
    }

    private VentaEntity buscar(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada: " + id));
    }

    private ProductoEntity buscarProductoActivo(Long productoId) {
        ProductoEntity p = productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + productoId));
        if (!p.getStatus()) {
            throw new ReglaNegocioException("El producto está inactivo");
        }
        return p;
    }

    @Transactional(readOnly = true)
    public ReporteVentasDTO reporteAdmin(ReporteFiltroDTO filtro) {
        return generarReporte(filtro);
    }

    @Transactional(readOnly = true)
    public ReporteVentasDTO reporteVendedor(Long vendedorId, LocalDate fecha) {
        ReporteFiltroDTO filtro = new ReporteFiltroDTO();
        filtro.setPeriodo(Periodo.DIA);
        filtro.setFecha(fecha);
        filtro.setVendedorId(vendedorId);
        return generarReporte(filtro);
    }

    private ReporteVentasDTO generarReporte(ReporteFiltroDTO f) {
        LocalDate[] rango = calcularRango(f);

        List<VentaDTO> ventas = ventaRepository
                .buscarParaReporte(rango[0], rango[1], f.getVendedorId(), f.getBloqueId(), f.getProductoId())
                .stream().map(ventaMapper::toDTO).toList();

        return ReporteVentasDTO.builder()
                .titulo(titulo(f.getPeriodo()))
                .desde(rango[0])
                .hasta(rango[1])
                .filtros(filtros(f))
                .agruparPor(f.getAgruparPor())
                .numeroVentas(ventas.size())
                .totalGeneral(sumar(ventas))
                .ventas(ventas)
                .resumen(agrupar(ventas, f.getAgruparPor()))
                .build();
    }

    private LocalDate[] calcularRango(ReporteFiltroDTO f) {
        LocalDate ancla = f.getFecha() != null ? f.getFecha() : LocalDate.now(ZONA);
        Periodo periodo = f.getPeriodo() != null ? f.getPeriodo() : Periodo.DIA;

        return switch (periodo) {
            case DIA -> new LocalDate[]{ancla, ancla};
            case SEMANA -> new LocalDate[]{ancla.with(DayOfWeek.MONDAY), ancla.with(DayOfWeek.SUNDAY)};
            case MES -> new LocalDate[]{ancla.withDayOfMonth(1), ancla.with(TemporalAdjusters.lastDayOfMonth())};
            case PERSONALIZADO -> {
                ReglaNegocioException.si(f.getDesde() == null || f.getHasta() == null,
                        "Indica la fecha inicial y la final");
                ReglaNegocioException.si(f.getDesde().isAfter(f.getHasta()),
                        "La fecha inicial no puede ser posterior a la final");
                yield new LocalDate[]{f.getDesde(), f.getHasta()};
            }
        };
    }

    private String titulo(Periodo periodo) {
        if (periodo == null) return "Reporte de ventas";
        return switch (periodo) {
            case DIA -> "Reporte diario de ventas";
            case SEMANA -> "Reporte semanal de ventas";
            case MES -> "Reporte mensual de ventas";
            case PERSONALIZADO -> "Reporte de ventas";
        };
    }

    private String filtros(ReporteFiltroDTO f) {
        List<String> partes = new ArrayList<>();
        if (f.getVendedorId() != null) {
            partes.add("Vendedor: " + vendedorRepository.findById(f.getVendedorId())
                    .map(v -> v.getNombre() + " " + v.getApellido_paterno()).orElse("desconocido"));
        }
        if (f.getBloqueId() != null) {
            partes.add("Bloque: " + bloqueRepository.findById(f.getBloqueId())
                    .map(BloqueEntity::getNombre).orElse("desconocido"));
        }
        if (f.getProductoId() != null) {
            partes.add("Producto: " + productoRepository.findById(f.getProductoId())
                    .map(ProductoEntity::getNombre).orElse("desconocido"));
        }
        return partes.isEmpty() ? "Todos los vendedores, bloques y productos" : String.join(" | ", partes);
    }

    private List<ResumenReporteDTO> agrupar(List<VentaDTO> ventas, AgruparPor por) {
        if (por == null || por == AgruparPor.NINGUNO) return List.of();

        Function<VentaDTO, String> clave = switch (por) {
            case VENDEDOR -> VentaDTO::getVendedorNombre;
            case BLOQUE -> VentaDTO::getBloqueNombre;
            case PRODUCTO -> VentaDTO::getProductoNombre;
            default -> v -> "";
        };

        return ventas.stream()
                .collect(Collectors.groupingBy(clave, TreeMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(e -> new ResumenReporteDTO(e.getKey(), e.getValue().size(), sumar(e.getValue())))
                .toList();
    }

    private BigDecimal sumar(List<VentaDTO> ventas) {
        return ventas.stream().map(VentaDTO::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}