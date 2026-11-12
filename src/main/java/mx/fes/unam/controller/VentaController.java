package mx.fes.unam.controller;

import mx.fes.unam.model.entities.venta.ReporteVentasDTO;
import mx.fes.unam.model.entities.venta.VentaDTO;
import mx.fes.unam.service.producto.ProductoService;
import mx.fes.unam.service.venta.PdfVentaService;
import mx.fes.unam.service.vendedor.VendedorService;
import mx.fes.unam.service.venta.VentaService;
import mx.fes.unam.util.ReglaNegocioException;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** Pantalla del vendedor: registrar ventas y descargar su reporte del día. */
@Controller
@RequestMapping("/venta")
public class VentaController {
    private final VentaService ventaService;
    private final ProductoService productoService;
    private final PdfVentaService pdfVentaService;
    private final VendedorService vendedorService;

    public VentaController(VentaService ventaService, ProductoService productoService,
                           PdfVentaService pdfVentaService, VendedorService vendedorService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.pdfVentaService = pdfVentaService;
        this.vendedorService = vendedorService;
    }

    @GetMapping("/registrar")
    public String registrar(@SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario, Model model) {
        String bloque = vendedorService.obtenerVendedor(usuario.id()).getNombreBloque();
        model.addAttribute("productos", productoService.listarActivosPorCategoria(bloque));
        model.addAttribute("nombreBloque", bloque);
        model.addAttribute("reporte", ventaService.reporteVendedor(usuario.id(), null));
        return "ventas/registrar_venta";
    }

    @PostMapping("/registrar")
    public String guardar(@ModelAttribute VentaDTO venta,
                          @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario,
                          RedirectAttributes flash) {
        ReglaNegocioException.si(venta.getProductoId() == null, "Selecciona un producto");
        ReglaNegocioException.si(venta.getCantidad() == null || venta.getCantidad() < 1,
                "La cantidad debe ser al menos 1");
        // El vendedor es siempre el de la sesión; el total lo calcula el servicio
        ventaService.registrarVenta(venta, usuario.id());
        flash.addFlashAttribute("mensaje", "Venta registrada");
        return "redirect:/venta/registrar";
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> pdf(@SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        ReporteVentasDTO reporte = ventaService.reporteVendedor(usuario.id(), fecha);
        byte[] pdf = pdfVentaService.generarReporte(reporte);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"reporte-ventas-" + reporte.getDesde() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
