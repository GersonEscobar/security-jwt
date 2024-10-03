package proyecto.dos.security_jwt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.dos.security_jwt.models.Marcaje;
import proyecto.dos.security_jwt.models.Usuarios;
import proyecto.dos.security_jwt.repositories.MarcajeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MarcajeService {

    @Autowired
    public MarcajeRepository marcajeRepository;

    public Marcaje registrarEntrada(Usuarios usuario) {
        Marcaje marcaje = new Marcaje();
        marcaje.setUsuario(usuario);
        marcaje.setHoraEntrada(LocalDateTime.now());
        return marcajeRepository.save(marcaje);
    }

    public Marcaje registrarSalida(Usuarios usuario) {
        List<Marcaje> marcajes = marcajeRepository.findByUsuario(usuario);
        if (!marcajes.isEmpty()) {
            Marcaje ultimoMarcaje = marcajes.get(marcajes.size() - 1);
            if (ultimoMarcaje.getHoraSalida() == null) {
                ultimoMarcaje.setHoraSalida(LocalDateTime.now());
                return marcajeRepository.save(ultimoMarcaje);
            }
        }
        return null;
    }

    public List<Marcaje> obtenerMarcajes(Usuarios usuario) {
        return marcajeRepository.findByUsuario(usuario);
    }

    public Marcaje obtenerUltimoMarcaje(Usuarios usuario) {
        return marcajeRepository.findTopByUsuarioOrderByIdDesc(usuario);
    }

    public List<Marcaje> obtenerTodosLosMarcajes() {
        return marcajeRepository.findAll();
    }

    public Page<Marcaje> obtenerMarcajesPaginados(Pageable pageable) {
        return marcajeRepository.findAll(pageable);
    }
}
