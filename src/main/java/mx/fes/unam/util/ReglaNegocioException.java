package mx.fes.unam.util;

public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) { super(mensaje); }

    public static void si(boolean condicion, String mensaje) {
        if (condicion) throw new ReglaNegocioException(mensaje);
    }
}
