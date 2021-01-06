package mx.com.tigo.grupopapelero.gateway.endpoint;


import lombok.extern.slf4j.Slf4j;
import mx.com.tigo.grupopapelero.gateway.exception.BadRequestException;
import mx.com.tigo.grupopapelero.gateway.exception.EmailAlreadyExistsException;
import mx.com.tigo.grupopapelero.gateway.exception.UsernameAlreadyExistsException;
import mx.com.tigo.grupopapelero.gateway.model.Profile;
import mx.com.tigo.grupopapelero.gateway.model.Role;
import mx.com.tigo.grupopapelero.gateway.model.User;
import mx.com.tigo.grupopapelero.gateway.payload.*;
import mx.com.tigo.grupopapelero.gateway.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@Slf4j
public class AuthEndpoint {

    @Autowired 
    private UserService userService;
    
  

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

  
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest payload) {
        log.info("creating user {}", payload.getEmail());

        User user = User
                .builder()
                .apellidoPaterno(payload.getApellidoPaterno())
                .apellidoMaterno(payload.getApellidoMaterno())
                .celular(payload.getCelular())
                .telefonoCasa(payload.getTelefonoCasa())
                .telefonoOficina(payload.getTelefonoOficina())
                .nombres(payload.getNombres())
                .email(payload.getEmail()) 
                .password(payload.getPassword())
                .userProfile(Profile
                        .builder()
                        .displayName(payload.getNombres())
                        .build())
                .build();

        try {
            userService.registerUser(user, Role.USER);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getEmail()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true,"User registered successfully"));
    }
    
    
    @PutMapping(value = "/users",consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyUser(@Valid @RequestBody UpdateRequest payload, Principal p) {
        
        User user = User
                .builder()
                .apellidoPaterno(payload.getApellidoPaterno())
                .apellidoMaterno(payload.getApellidoMaterno())
                .celular(payload.getCelular())
                .telefonoCasa(payload.getTelefonoCasa())
                .telefonoOficina(payload.getTelefonoOficina())
                .nombres(payload.getNombres())
                .userProfile(Profile
                        .builder()
                        .displayName(payload.getNombres())
                        .build())
                .build();

        userService.modify(user, p.getName());

        return ResponseEntity
                .noContent().build();
    }

}
