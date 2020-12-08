package mx.com.tigo.grupopapelero.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.tigo.grupopapelero.gateway.model.PasswordResetToken;


@Repository
public interface RepositorioPasswordResetToken extends JpaRepository<PasswordResetToken, Integer>{

	PasswordResetToken findByTokenAndUuid(String token, String uuid); 

	
}
