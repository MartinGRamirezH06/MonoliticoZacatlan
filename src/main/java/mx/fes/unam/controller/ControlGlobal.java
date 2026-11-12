package mx.fes.unam.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mx.fes.unam.util.ReglaNegocioException;
import mx.fes.unam.util.UsuarioSesion;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;

@ControllerAdvice
public class ControlGlobal {

    /** El usuario de la sesión queda disponible como ${usuario} en todas las plantillas. */
    @ModelAttribute("usuario")
    public UsuarioSesion usuario(HttpSession sesion) {
        return (UsuarioSesion) sesion.getAttribute(UsuarioSesion.ATRIBUTO);
    }

    /** El cliente nunca decide el id ni el status: aunque los mande en el formulario, se ignoran. */
    @InitBinder
    public void bloquearCampos(WebDataBinder binder) {
        binder.setDisallowedFields("id", "status");
    }

    /** Errores de negocio: se regresa a la página de origen con el mensaje. */
    @ExceptionHandler({ReglaNegocioException.class, EntityNotFoundException.class})
    public String manejarError(RuntimeException ex, HttpServletRequest request) {
        RequestContextUtils.getOutputFlashMap(request).put("error", ex.getMessage());
        String origen = request.getHeader("Referer");
        return "redirect:" + (origen != null ? origen : "/");
    }
}
