package mx.fes.unam.controller;

import mx.fes.unam.model.entities.vendedor.Rol;
import mx.fes.unam.model.entities.venta.ReporteFiltroDTO;
import mx.fes.unam.model.entities.venta.ReporteVentasDTO;
import mx.fes.unam.service.bloque.BloqueService;
import mx.fes.unam.service.producto.ProductoService;
import mx.fes.unam.service.vendedor.VendedorService;
import mx.fes.unam.service.venta.PdfVentaService;
import mx.fes.unam.service.venta.VentaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/** Reportes del administrador con filtros (periodo, vendedor, bloque, producto). */
@Controller
@RequestMapping("/admin/reportes")
public class ReporteAdminController {
    private final VentaService ventaService;
    private final PdfVentaService pdfVentaService;
    private final VendedorService vendedorService;
    private final BloqueService bloqueService;
    private final ProductoService productoService;

    public ReporteAdminController(VentaService ventaService, PdfVentaService pdfVentaService,
                                  VendedorService vendedorService, BloqueService bloqueService,
                                  ProductoService productoService) {
        this.ventaService = ventaService;
        this.pdfVentaService = pdfVentaService;
        this.vendedorService = vendedorService;
        this.bloqueService = bloqueService;
        this.productoService = productoService;
    }

    @GetMapping
    public String ver(@ModelAttribute("filtro") ReporteFiltroDTO filtro, Model model) {
        model.addAttribute("reporte", ventaService.reporteAdmin(filtro));
        // Solo vendedores: los administradores no aparecen en el filtro de reportes
        model.addAttribute("vendedores", vendedorService.listarVendedores(false).stream()
                .filter(v -> v.getRol() == Rol.VENDEDOR)
                .toList());
        model.addAttribute("bloques", bloqueService.listarBloques(false));
        model.addAttribute("productos", productoService.listarProductos(false));
        return "admin/reportes";
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> pdf(@ModelAttribute ReporteFiltroDTO filtro) {
        ReporteVentasDTO reporte = ventaService.reporteAdmin(filtro);
        byte[] pdf = pdfVentaService.generarReporte(reporte);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"reporte-ventas-" + reporte.getDesde() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
