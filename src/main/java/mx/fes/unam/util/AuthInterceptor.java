package mx.fes.unam.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    private final boolean soloAdmin;

    public AuthInterceptor(boolean soloAdmin) {
        this.soloAdmin = soloAdmin;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession sesion = request.getSession(false);
        UsuarioSesion usuario = sesion == null ? null
                : (UsuarioSesion) sesion.getAttribute(UsuarioSesion.ATRIBUTO);

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        if (soloAdmin && !usuario.esAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo el administrador puede entrar aquí");
            return false;
        }
        return true;
    }
}
