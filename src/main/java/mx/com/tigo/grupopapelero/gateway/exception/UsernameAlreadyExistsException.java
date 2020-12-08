package mx.com.tigo.grupopapelero.gateway.exception;


public class UsernameAlreadyExistsException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3459191635538297073L;

	public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
