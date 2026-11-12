package mx.fes.unam.controller;

import mx.fes.unam.model.entities.bloque.BloqueDTO;
import mx.fes.unam.service.bloque.BloqueService;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bloque")
public class BloqueController {
    private static final String LISTA = "redirect:/bloque/listar-bloque";
    private final BloqueService bloqueService;

    public BloqueController(BloqueService bloqueService) {
        this.bloqueService = bloqueService;
    }

    @GetMapping("/listar-bloque")
    public String listar_bloque(Model model){
        model.addAttribute("bloques", bloqueService.listarBloques(false));
        return"bloque/lista_bloque";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute BloqueDTO bloque,
                        @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                        RedirectAttributes flash) {
        bloque.setStatus(true);
        bloqueService.crearBloque(bloque, admin.id());
        flash.addFlashAttribute("mensaje", "Bloque creado");
        return LISTA;
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id, @ModelAttribute BloqueDTO bloque,
                         @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                         RedirectAttributes flash) {
        bloqueService.actualizarBloque(id, bloque, admin.id());
        flash.addFlashAttribute("mensaje", "Bloque actualizado");
        return LISTA;
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id,
                             @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                             RedirectAttributes flash) {
        bloqueService.desactivarBloque(id, admin.id());
        flash.addFlashAttribute("mensaje", "Bloque desactivado");
        return LISTA;
    }

    @PostMapping("/{id}/reactivar")
    public String reactivar(@PathVariable Long id,
                            @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                            RedirectAttributes flash) {
        bloqueService.reactivarBloque(id, admin.id());
        flash.addFlashAttribute("mensaje", "Bloque reactivado");
        return LISTA;
    }
}
