package gk.gk.Exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(long id) {
        super("Could not find orders " + id);
    }
}
