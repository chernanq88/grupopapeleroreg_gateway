package mx.com.tigo.grupopapelero.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.com.tigo.grupopapelero.gateway.model.ProyeccionUsuarios;
import mx.com.tigo.grupopapelero.gateway.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String username); 

    Boolean existsByEmail(String email);
    
    @Query(nativeQuery = true, value="select * from administracion_usuarios "
    		+ "      where nombres    like %?1% or" 
    		+ "      apellidoPaterno  like %?1% or"
    		+ "      telefonoCelular  like %?1% or"
    		+ "      telefonoCasa     like %?1% or"
    		+ "      telefonoOficina  like %?1% or"
    		+ "      nombreUsuario    like %?1% " )
    List<ProyeccionUsuarios> listarClientesInput(String input);
    
    @Query(nativeQuery = true, value="select * from administracion_usuarios" )
    List<ProyeccionUsuarios> listarClientes();
    
}
