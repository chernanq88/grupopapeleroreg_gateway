package mx.com.tigo.grupopapelero.gateway.exception;

public class TokenInvalidoOExpiradoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -653954868146734570L;

	public TokenInvalidoOExpiradoException(Throwable e) {
		super(e);
	}
	
	public TokenInvalidoOExpiradoException(String mensaje) {
		super(mensaje);
	}

}
