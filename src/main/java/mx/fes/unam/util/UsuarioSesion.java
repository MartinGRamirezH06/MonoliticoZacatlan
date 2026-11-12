package mx.fes.unam.util;

import mx.fes.unam.model.entities.vendedor.Rol;

import java.io.Serializable;

/** Datos mínimos del usuario que inició sesión (se guardan en HttpSession). */
public record UsuarioSesion(Long id, String nombre, Rol rol) implements Serializable {
    public static final String ATRIBUTO = "usuario";

    public boolean esAdmin() {
        return rol == Rol.ADMIN;
    }
}
