package mx.com.tigo.grupopapelero.gateway.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacebookLoginRequest implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9171054868792335268L;
	@NotBlank
    private String accessToken;
}
