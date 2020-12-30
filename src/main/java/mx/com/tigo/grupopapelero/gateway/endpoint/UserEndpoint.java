package mx.com.tigo.grupopapelero.gateway.endpoint;

import lombok.extern.slf4j.Slf4j;
import mx.com.tigo.grupopapelero.gateway.exception.*;
import mx.com.tigo.grupopapelero.gateway.model.InstaUserDetails;
import mx.com.tigo.grupopapelero.gateway.model.User;
import mx.com.tigo.grupopapelero.gateway.payload.*;
import mx.com.tigo.grupopapelero.gateway.service.UserService;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("email") String email) {
        log.info("retrieving user {}", email);

        return  userService
                .findByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new ResourceNotFoundException(email));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity
                .ok(userService.findAll());
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserSummary getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails, Principal p) {
        return UserSummary
                .builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .apellidoPaterno(userDetails.getApellidoPaterno()) 
                .apellidoMaterno(userDetails.getApellidoMaterno())
                .telefonoCasa(userDetails.getTelefonoCasa())
                .telefonoOficina(userDetails.getTelefonoOficina())
                .nombres(userDetails.getUserProfile().getDisplayName())
                .profilePicture(userDetails.getUserProfile().getProfilePictureUrl())
                .build();
    }

    @GetMapping(value = "/users/summary/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("email") String email) {
        log.info("retrieving user {}", email);

        return  userService
                .findByEmail(email)
                .map(user -> ResponseEntity.ok(convertTo(user)))
                .orElseThrow(() -> new ResourceNotFoundException(email));
    }
    
    @PostMapping(value="/users/changepass")
    @ResponseStatus(HttpStatus.OK)
    public void cambiarPassword(@RequestBody CambioPassword cambiopass,@AuthenticationPrincipal InstaUserDetails userDetails){
  
    	userService.cambiarPassword(userDetails.getUsername(), cambiopass.getViejoPass(), cambiopass.getNuevoPass());
    	
    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getId())
                .apellidoMaterno(user.getApellidoMaterno())
                .apellidoPaterno(user.getApellidoPaterno())
                .celular(user.getCelular())
                .telefonoCasa(user.getTelefonoCasa())
                .telefonoOficina(user.getTelefonoOficina())
                .nombres(user.getUserProfile().getDisplayName())
                .profilePicture(user.getUserProfile().getProfilePictureUrl())
                .build();
    }
    
    @PostMapping(value = "/passwordOlvidado", produces = "application/json")
	public ResponseEntity<Object> cambiarPassword(
			@RequestBody ResetPassword resetObj,@AuthenticationPrincipal InstaUserDetails userDetails) {

		try {
			userService.enviarTokenDeRecuperacionAUsuario(userDetails.getEmail());
			
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			log.error("Error",e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/guardarPasswordRecuperado")
	public ResponseEntity<Object> guardarContrasenaRecuperada(@RequestBody @Valid PasswordRecuperadoEntity reset) {

		log.info("Guarda password recuperado");
		userService.guardarPasswordRecuperado(reset);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}