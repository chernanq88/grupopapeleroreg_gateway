package mx.com.tigo.grupopapelero.gateway.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;
import mx.com.tigo.grupopapelero.gateway.exception.EmailAlreadyExistsException;
import mx.com.tigo.grupopapelero.gateway.exception.TokenInvalidoOExpiradoException;
import mx.com.tigo.grupopapelero.gateway.freemarker.model.AvisoRecuperacionContrasena;
import mx.com.tigo.grupopapelero.gateway.model.PasswordResetToken;
import mx.com.tigo.grupopapelero.gateway.model.ProyeccionUsuarios;
import mx.com.tigo.grupopapelero.gateway.model.Role;
import mx.com.tigo.grupopapelero.gateway.model.User;
import mx.com.tigo.grupopapelero.gateway.payload.PasswordRecuperadoEntity;
import mx.com.tigo.grupopapelero.gateway.repository.ProfileRepository;
import mx.com.tigo.grupopapelero.gateway.repository.RepositorioPasswordResetToken;
import mx.com.tigo.grupopapelero.gateway.repository.RoleRepository;
import mx.com.tigo.grupopapelero.gateway.repository.UserRepository;

@Service
@Log4j2
public class UserService extends ServicioAbstracto {

	private static final String TOKEN_INVALID = "invalidToken";
	private static final String TOKEN_EXPIRED = "expired";
	private static final int EXPIRACION = 10;
	
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RepositorioPasswordResetToken passwordResetToken;
    
    @Autowired
    private Configuration cfg;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Value("${grupopapeleroreg.mensajesCorreo.asuntoPasswordOlvidado}")
    private String asuntoPasswordOlvidado;

    public String loginUser(String username, String password) {
       Authentication authentication = authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken(username, password));

       return tokenProvider.generateToken(authentication);
    }

    @Transactional
    public User registerUser(User user, Role role) {
        log.info("registering user {}", user.getEmail());
        if (user.getId()==null) {
        	user.setId(UUID.randomUUID().toString());
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setCreatedAt(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());
        
        Optional<Role> roleBd=roleRepository.findById(role.getId());
        user.getRoles().add(roleBd.orElseThrow());

        profileRepository.save(user.getUserProfile());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }
    
    public Optional<User> findByEmail(String email) {
        log.info("retrieving user {}", email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        log.info("retrieving user {}", id);
        return userRepository.findById(id);
    }
    
	public void cambiarPassword(String email, String passwordViejo, String passwordNuevo) {

		Optional<User> cliente = userRepository.findByEmail(email);

		if (cliente.isPresent() && passwordEncoder.matches(passwordViejo, cliente.get().getPassword())) {
			cliente.get().setPassword(passwordEncoder.encode(passwordNuevo));
			userRepository.save(cliente.get());
			
		} else {
			throw new BadCredentialsException("Usuario no encontrado o password no coincide");
		}	
	}
	
	
	public PasswordResetToken obtenerTokenRecuperacionPassword(String username) {
	
		Optional<User> cliente = findByEmail(username);
		
		
		PasswordResetToken miToken = new PasswordResetToken();
		miToken.setToken(UUID.randomUUID().toString());
		miToken.setUsuario(cliente.orElseThrow());
		miToken.setExpiracion(calcularFechaExpiracion(getExpiracion())); 
		miToken.setUuid(UUID.randomUUID().toString());
		passwordResetToken.save(miToken);
		return miToken;
	}
	
	public void guardarPasswordRecuperado(PasswordRecuperadoEntity reset) {
		String respuesta = validatePasswordResetToken(reset.getToken(), reset.getUuid());
		if (TOKEN_EXPIRED.equals(respuesta) || TOKEN_INVALID.equals(respuesta)) {
			throw new TokenInvalidoOExpiradoException("Token invalido o expirado");
		} else {
			User usuario = findByEmail(respuesta).orElseThrow();
			usuario.setPassword(
					passwordEncoder.encode(reset.getNuevaContrasena()));
			userRepository.save(usuario);
		}

	}
	
	private String validatePasswordResetToken(String token, String uuid) {
		final PasswordResetToken passToken = 
				passwordResetToken.findByTokenAndUuid(token, uuid);

		if (isTokenFound(passToken) ) {
			if (isTokenExpired(passToken)) {
				return TOKEN_EXPIRED;
			}
		} else {
			return TOKEN_INVALID;  
			
		}
		return passToken.getUsuario().getEmail();
			
	}

	private boolean isTokenFound(PasswordResetToken passToken) {
		return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
		final Calendar cal = Calendar.getInstance();
		return passToken.getExpiracion().before(cal.getTime());
	}
	
	
	protected int getExpiracion(){
		return EXPIRACION;
	}
	
	protected String getTokenExpired(){
		return TOKEN_EXPIRED;
	}

	protected String getTokenInvalid(){
		return TOKEN_INVALID;
	}
	
	private Date calcularFechaExpiracion(final int expiryTimeInMinutes) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime()); 
	}

	public void enviarTokenDeRecuperacionAUsuario(String email) throws IOException, TemplateException{
		
		User usuario = findByEmail(email).orElseThrow();

		PasswordResetToken miToken = new PasswordResetToken();
	
		miToken.setToken(UUID.randomUUID().toString());
		miToken.setUsuario(usuario);
		miToken.setExpiracion(calcularFechaExpiracion(EXPIRACION)); 
		miToken.setUuid(UUID.randomUUID().toString());
		passwordResetToken.save(miToken);

		AvisoRecuperacionContrasena aviso=new AvisoRecuperacionContrasena();
		aviso.setApp("Triver");
		aviso.setNombre(usuario.getNombres());
		aviso.setTengaEnCuenta("getServicioEtiquetas().obtenerPorId(CAMBIO_CONTRASENA_TENGA_EN_CUENTA");
		aviso.setConfirmarAccion("getServicioEtiquetas().obtenerPorId(CAMBIO_CONTRASENA_CONFIRMAR_ACCION");
		aviso.setHaSolicitadoCambio("getServicioEtiquetas().obtenerPorId(CAMBIO_CONTRASENA_HA_SOLICITADO");
		aviso.setIgnoreCorreoElectronico("getServicioEtiquetas().obtenerPorId(CAMBIO_CONTRASENA_IGNORE_CORREO_ELECTRONICO");
		aviso.setUriCambioPass("uriCambioPass.get().getValor()"); 
	
		aviso.setToken(miToken.getToken());
		aviso.setMensaje("getServicioEtiquetas().obtenerPorId(CAMBIO_CONTRASENA_CONFIRMAR_SOLICITUD");
		aviso.setDescripcionMensaje("getServicioEtiquetas().obtenerPorId(CAMBIO_CONTRASENA_HA_SOLICITADO)");
		aviso.setUuid(miToken.getUuid());
		
		
		Template temp = cfg.getTemplate("recuperacionContrasena.html");
		StringWriter out = new StringWriter();
		temp.process(aviso, out);

		enviarCorreo(usuario.getEmail(), asuntoPasswordOlvidado,
				out.toString(),null,null);

	}

	@Transactional
	public void modify(User user, String email) {

		Optional<User> opt= userRepository.findByEmail(email);
		User entity=opt.orElseThrow();	
		entity.setNombres(user.getNombres());
		entity.setApellidoMaterno(user.getApellidoMaterno());
		entity.setApellidoPaterno(user.getApellidoPaterno());
		entity.setCelular(user.getCelular());
		entity.setTelefonoCasa(user.getTelefonoCasa());
		entity.setTelefonoOficina(user.getTelefonoOficina());
		entity.setUpdatedAt(new Date());
		
        userRepository.save(entity);
	}

	public List<ProyeccionUsuarios> listarClientes(String input) {
		
		if (input==null) {
			return userRepository.listarClientes();
		}else {
			return userRepository.listarClientesInput(input);
		}
		
		
	}

}
