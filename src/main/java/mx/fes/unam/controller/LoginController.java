package mx.fes.unam.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.service.vendedor.VendedorService;
import mx.fes.unam.util.ReglaNegocioException;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private final VendedorService vendedorService;

    public LoginController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession sesion){
        if (sesion.getAttribute(UsuarioSesion.ATRIBUTO) != null) {
            return "redirect:/";
        }
        return "login/login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String correo,
                                @RequestParam String password,
                                HttpServletRequest request,
                                RedirectAttributes flash) {
        try {
            VendedorDTO v = vendedorService.autenticar(correo.trim(), password);
            HttpSession sesion = request.getSession();
            request.changeSessionId();
            sesion.setAttribute(UsuarioSesion.ATRIBUTO,
                    new UsuarioSesion(v.getId(), v.getNombre() + " " + v.getApellido_paterno(), v.getRol(),
                            v.getNombreBloque()));
            return "redirect:/";
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/login";
    }
}
