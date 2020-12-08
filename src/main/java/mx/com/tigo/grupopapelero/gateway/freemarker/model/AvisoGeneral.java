package mx.com.tigo.grupopapelero.gateway.freemarker.model;

import lombok.Data;

@Data
public class AvisoGeneral {

	private String mensaje;
	private String descripcionMensaje;
	private String ignoreCorreoElectronico;
	private String empresa;
	private String nombre;
	private String apellidoPaterno;
	private String app;
	private Integer idRequerimiento;
	
}
