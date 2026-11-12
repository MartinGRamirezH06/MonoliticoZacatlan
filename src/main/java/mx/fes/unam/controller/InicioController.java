package mx.fes.unam.controller;

import mx.fes.unam.util.UsuarioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class InicioController {
    @GetMapping("/")
    public String inicio(@SessionAttribute(UsuarioSesion.ATRIBUTO) UsuarioSesion usuario) {
        return usuario.esAdmin() ? "redirect:/bloque/listar-bloque" : "redirect:/venta/registrar";
    }
}
