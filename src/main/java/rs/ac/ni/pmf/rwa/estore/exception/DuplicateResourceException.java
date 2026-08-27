package rs.ac.ni.pmf.rwa.estore.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(final String message) {
        super(message);
    }
}
