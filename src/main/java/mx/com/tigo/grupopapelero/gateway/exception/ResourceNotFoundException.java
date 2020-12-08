package mx.com.tigo.grupopapelero.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3703711898287784649L;

	public ResourceNotFoundException(String resource) {
        super(String.format("Resource %s not found", resource));
    }
}
