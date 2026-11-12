package mx.fes.unam.controller;

import mx.fes.unam.model.entities.vendedor.Rol;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.service.bloque.BloqueService;
import mx.fes.unam.service.vendedor.VendedorService;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Gestión de vendedores. Aquí SIEMPRE se trabaja con rol VENDEDOR; los admins van en /admin/administradores. */
@Controller
@RequestMapping("/vendedor")
public class VendedorController {
    private static final String LISTA = "redirect:/vendedor/listar-vendedores";
    private final VendedorService vendedorService;
    private final BloqueService bloqueService;

    public VendedorController(VendedorService vendedorService, BloqueService bloqueService) {
        this.vendedorService = vendedorService;
        this.bloqueService = bloqueService;
    }

    @GetMapping("/listar-vendedores")
    public String listar_vendores(Model model){
        model.addAttribute("vendedores", vendedorService.listarPorRol(Rol.VENDEDOR));
        model.addAttribute("bloques", bloqueService.listarBloques(true));
        return "vendedor/listar_vendedores";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute VendedorDTO vendedor,
                        @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                        RedirectAttributes flash) {
        vendedor.setRol(Rol.VENDEDOR);   // por defecto y siempre: vendedor
        vendedor.setStatus(true);
        vendedorService.crearVendedor(vendedor, admin.id());
        flash.addFlashAttribute("mensaje", "Vendedor creado");
        return LISTA;
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id, @ModelAttribute VendedorDTO vendedor,
                         @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                         RedirectAttributes flash) {
        vendedorService.validarRol(id, Rol.VENDEDOR);
        vendedor.setRol(Rol.VENDEDOR);
        vendedorService.actualizarVendedor(id, vendedor, admin.id());
        flash.addFlashAttribute("mensaje", "Vendedor actualizado");
        return LISTA;
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id,
                             @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                             RedirectAttributes flash) {
        vendedorService.validarRol(id, Rol.VENDEDOR);
        vendedorService.desactivarVendedor(id, admin.id());
        flash.addFlashAttribute("mensaje", "Vendedor desactivado");
        return LISTA;
    }

    @PostMapping("/{id}/reactivar")
    public String reactivar(@PathVariable Long id,
                            @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                            RedirectAttributes flash) {
        vendedorService.validarRol(id, Rol.VENDEDOR);
        vendedorService.reactivarVendedor(id, admin.id());
        flash.addFlashAttribute("mensaje", "Vendedor reactivado");
        return LISTA;
    }
}
