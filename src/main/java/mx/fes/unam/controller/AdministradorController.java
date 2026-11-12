package mx.fes.unam.controller;

import mx.fes.unam.model.entities.vendedor.Rol;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.service.vendedor.VendedorService;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Gestión de administradores (solo los puede ver el admin: /admin/** está protegido en ConfigSeguridad). */
@Controller
@RequestMapping("/admin/administradores")
public class AdministradorController {
    private static final String LISTA = "redirect:/admin/administradores";
    private final VendedorService vendedorService;

    public AdministradorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("administradores", vendedorService.listarPorRol(Rol.ADMIN));
        return "admin/lista_administradores";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute VendedorDTO admin,
                        @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion sesion,
                        RedirectAttributes flash) {
        admin.setRol(Rol.ADMIN);
        admin.setBloqueId(null);   // los admin no pertenecen a un bloque
        admin.setStatus(true);
        vendedorService.crearVendedor(admin, sesion.id());
        flash.addFlashAttribute("mensaje", "Administrador creado");
        return LISTA;
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id, @ModelAttribute VendedorDTO admin,
                         @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion sesion,
                         RedirectAttributes flash) {
        vendedorService.validarRol(id, Rol.ADMIN);
        admin.setRol(Rol.ADMIN);
        vendedorService.actualizarVendedor(id, admin, sesion.id());
        flash.addFlashAttribute("mensaje", "Administrador actualizado");
        return LISTA;
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id,
                             @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion sesion,
                             RedirectAttributes flash) {
        vendedorService.validarRol(id, Rol.ADMIN);
        vendedorService.desactivarVendedor(id, sesion.id());   // no permite desactivarte a ti mismo
        flash.addFlashAttribute("mensaje", "Administrador desactivado");
        return LISTA;
    }

    @PostMapping("/{id}/reactivar")
    public String reactivar(@PathVariable Long id,
                            @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion sesion,
                            RedirectAttributes flash) {
        vendedorService.validarRol(id, Rol.ADMIN);
        vendedorService.reactivarVendedor(id, sesion.id());
        flash.addFlashAttribute("mensaje", "Administrador reactivado");
        return LISTA;
    }
}
