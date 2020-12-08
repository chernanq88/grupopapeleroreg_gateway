package mx.com.tigo.grupopapelero.gateway.freemarker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AvisoRecuperacionContrasena extends AvisoGeneral{

	private String haSolicitadoCambio;
	private String tengaEnCuenta;
	private String confirmarAccion;
	private String uriCambioPass;
	private String token;
	private String uuid;
	
}
