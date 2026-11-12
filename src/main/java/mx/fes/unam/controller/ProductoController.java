package mx.fes.unam.controller;

import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.service.producto.ProductoService;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/producto")
public class ProductoController {
    private static final String LISTA = "redirect:/producto/listar-productos";
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/listar-productos")
    public String listar_productos(Model model){
        model.addAttribute("productos", productoService.listarProductos(false));
        return "producto/lista_producto";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute ProductoDTO producto,
                        @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                        RedirectAttributes flash) {
        productoService.crearProducto(producto, admin.id());
        flash.addFlashAttribute("mensaje", "Producto creado");
        return LISTA;
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id, @ModelAttribute ProductoDTO producto,
                         @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                         RedirectAttributes flash) {
        productoService.actualizarProducto(id, producto, admin.id());
        flash.addFlashAttribute("mensaje", "Producto actualizado");
        return LISTA;
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id,
                             @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                             RedirectAttributes flash) {
        productoService.desactivarProducto(id, admin.id());
        flash.addFlashAttribute("mensaje", "Producto desactivado");
        return LISTA;
    }

    @PostMapping("/{id}/reactivar")
    public String reactivar(@PathVariable Long id,
                            @SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion admin,
                            RedirectAttributes flash) {
        productoService.reactivarProducto(id, admin.id());
        flash.addFlashAttribute("mensaje", "Producto reactivado");
        return LISTA;
    }
}
