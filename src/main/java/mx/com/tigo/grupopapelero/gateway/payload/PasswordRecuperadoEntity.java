package mx.com.tigo.grupopapelero.gateway.payload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PasswordRecuperadoEntity {
	 
	@NotNull
    private String token;
 
    @NotNull
    private String nuevaContrasena;
    
    @NotNull
    private String uuid;
}
