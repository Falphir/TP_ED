package lei.estg.dataStructures.exceptions;

public class ConcurrentModificationException extends Throwable {
    public ConcurrentModificationException(String message) {
        super(message);
    }
}
