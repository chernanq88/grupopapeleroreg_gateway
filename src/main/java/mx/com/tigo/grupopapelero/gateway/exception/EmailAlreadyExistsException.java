package mx.com.tigo.grupopapelero.gateway.exception;


public class EmailAlreadyExistsException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2738982687119532204L;

	public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
