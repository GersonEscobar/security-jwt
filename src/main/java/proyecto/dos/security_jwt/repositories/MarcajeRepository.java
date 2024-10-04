package proyecto.dos.security_jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto.dos.security_jwt.models.Marcaje;
import proyecto.dos.security_jwt.models.Usuarios;

import java.util.List;
import java.util.Optional;

public interface MarcajeRepository extends JpaRepository<Marcaje, Long> {
    List<Marcaje> findByUsuario(Usuarios usuario);
    Marcaje findTopByUsuarioOrderByIdDesc(Usuarios usuario);


//    List<Marcaje> findByUsuario_username(String username);
//    @Query("SELECT m FROM Marcaje m WHERE m.usuario = ?1 AND m.fechaHora BETWEEN ?2 AND ?3")
//    List<Marcaje> findByUsuarioAndFechaHoraBetween(Usuarios usuario, LocalDateTime startDate, LocalDateTime endDate);
//
//    List<Marcaje> findAll();
}
