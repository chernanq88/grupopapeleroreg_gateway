package mx.com.tigo.grupopapelero.gateway.payload;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateRequest {

    
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
