package httpserver.routing;

public class FormManagerException extends RuntimeException {
    public FormManagerException(Throwable cause) {
        super("Cannot access form: ", cause);
    }
}
