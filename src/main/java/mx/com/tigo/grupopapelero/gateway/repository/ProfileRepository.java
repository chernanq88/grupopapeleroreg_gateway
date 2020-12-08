package mx.com.tigo.grupopapelero.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.tigo.grupopapelero.gateway.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

}
