package mx.com.tigo.grupopapelero.gateway.payload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ResetPassword {

	@NotNull
	private String correo;
	
}
