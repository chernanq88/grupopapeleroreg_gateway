package mx.com.tigo.grupopapelero.gateway.payload;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CambioPassword implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2510335072790284105L;

	@NotNull
	private String nuevoPass;
	
	@NotNull
	private String viejoPass;

}
