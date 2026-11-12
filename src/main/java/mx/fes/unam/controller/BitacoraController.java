package mx.fes.unam.controller;

import mx.fes.unam.model.entities.vendedor.Rol;
import mx.fes.unam.service.bitacora.BitacoraService;
import mx.fes.unam.service.vendedor.VendedorService;
import mx.fes.unam.util.ReglaNegocioException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/** Consulta de la bitácora: qué administrador creó, editó, desactivó o reactivó cada registro. */
@Controller
@RequestMapping("/admin/bitacora")
public class BitacoraController {
    private static final ZoneId ZONA = ZoneId.of("America/Mexico_City");
    private static final List<String> ENTIDADES = List.of("BLOQUE", "PRODUCTO", "VENDEDOR", "VENTA");

    private final BitacoraService bitacoraService;
    private final VendedorService vendedorService;

    public BitacoraController(BitacoraService bitacoraService, VendedorService vendedorService) {
        this.bitacoraService = bitacoraService;
        this.vendedorService = vendedorService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String entidad,
                         @RequestParam(required = false) Long adminId,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                         Model model) {
        LocalDate hoy = LocalDate.now(ZONA);
        if (hasta == null) hasta = hoy;
        if (desde == null) desde = hasta.minusDays(30);
        ReglaNegocioException.si(desde.isAfter(hasta), "La fecha inicial no puede ser posterior a la final");
        String ent = (entidad == null || entidad.isBlank()) ? null : entidad.toUpperCase();

        model.addAttribute("registros", bitacoraService.buscar(ent, adminId, desde, hasta));
        model.addAttribute("entidades", ENTIDADES);
        model.addAttribute("administradores", vendedorService.listarPorRol(Rol.ADMIN));
        model.addAttribute("entidad", ent);
        model.addAttribute("adminId", adminId);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "admin/bitacora";
    }
}
