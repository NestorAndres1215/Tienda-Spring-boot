package evaluacioncontinua.exception;


import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> errores;

    public ValidationException(List<String> errores) {
        super("Error de validación");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}