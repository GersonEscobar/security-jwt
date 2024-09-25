package proyecto.dos.security_jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.dos.security_jwt.models.Roles;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName (String name);
}
