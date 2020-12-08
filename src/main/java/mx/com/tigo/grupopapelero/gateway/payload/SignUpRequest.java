package mx.com.tigo.grupopapelero.gateway.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {


    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    
    @NotBlank
    private String nombres;
    
    @NotBlank
    private String apellidoPaterno;
    
    @NotBlank
    
    private String apellidoMaterno;
    
    @NotBlank
    private String celular;
    
    
    private String telefonoCasa;
    private String telefonoOficina;
}
