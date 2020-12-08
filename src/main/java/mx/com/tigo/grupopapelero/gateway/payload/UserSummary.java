package mx.com.tigo.grupopapelero.gateway.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {

    private String id;
    private String username;
    private String profilePicture;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String celular;
    private String telefonoOficina;
    private String telefonoCasa;
    private String locale;
}
