package mx.com.tigo.grupopapelero.gateway.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8390046925163168736L;

	public InternalServerException(String message) {
        super(message);
    }
}
