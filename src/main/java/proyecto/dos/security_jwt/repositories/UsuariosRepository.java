package proyecto.dos.security_jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.dos.security_jwt.models.Usuarios;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository <Usuarios, Long> {

    Optional<Usuarios> findByUsername(String username);

    Boolean existsByUsername(String username);

}
