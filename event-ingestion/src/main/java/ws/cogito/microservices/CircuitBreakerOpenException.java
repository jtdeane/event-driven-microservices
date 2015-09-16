package ws.cogito.microservices;

public final class CircuitBreakerOpenException extends Exception {

	private static final long serialVersionUID = -6147511481360982113L;
	 
    private String message = "Circuit Breaker is Open!";
 
    public CircuitBreakerOpenException() {
        super();
    }
 
    public CircuitBreakerOpenException(String message) {
        super(message);
        this.message = message;
    }
 
    public CircuitBreakerOpenException(Throwable cause) {
        super(cause);
    }
 
    @Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
}