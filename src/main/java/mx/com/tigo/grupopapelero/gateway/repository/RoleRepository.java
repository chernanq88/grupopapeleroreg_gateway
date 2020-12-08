package mx.com.tigo.grupopapelero.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.tigo.grupopapelero.gateway.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
