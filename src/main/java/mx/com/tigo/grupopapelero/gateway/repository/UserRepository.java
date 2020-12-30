package mx.com.tigo.grupopapelero.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.tigo.grupopapelero.gateway.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String username); 

    Boolean existsByEmail(String email);
}
