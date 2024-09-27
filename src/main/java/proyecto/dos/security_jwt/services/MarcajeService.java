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
//    @Autowired
//    private MarcajeRepository marcajeRepository;
//
//    public Marcaje registrarEntrada(Usuarios usuario) {
//        List<Marcaje> marcajesDelDia = marcajeRepository.findByUsuarioAndFechaHoraBetween(
//                usuario,
//                LocalDate.now().atStartOfDay(),
//                LocalDate.now().plusDays(1).atStartOfDay()
//        );
//
//        // Solo se permite registrar una entrada si no hay entradas previas en el día
//        if (marcajesDelDia.stream().noneMatch(Marcaje::isEsEntrada)) {
//            Marcaje marcaje = new Marcaje();
//            marcaje.setUsuario(usuario);
//            marcaje.setFechaHora(LocalDateTime.now());
//            marcaje.setEsEntrada(true);
//            return marcajeRepository.save(marcaje);
//        }
//
//        return null; // o lanzar una excepción si se desea
//    }
//
//    public Marcaje registrarSalida(Usuarios usuario) {
//        List<Marcaje> marcajesDelDia = marcajeRepository.findByUsuarioAndFechaHoraBetween(
//                usuario,
//                LocalDate.now().atStartOfDay(),
//                LocalDate.now().plusDays(1).atStartOfDay()
//        );
//
//        // Solo se permite registrar una salida si hay al menos una entrada
//        if (marcajesDelDia.stream().anyMatch(Marcaje::isEsEntrada)) {
//            Marcaje marcaje = new Marcaje();
//            marcaje.setUsuario(usuario);
//            marcaje.setFechaHora(LocalDateTime.now());
//            marcaje.setEsEntrada(false);
//            return marcajeRepository.save(marcaje);
//        }
//
//        return null; // o lanzar una excepción si se desea
//    }
//
//    public List<Marcaje> obtenerMarcajesPorUsuario(Usuarios usuario) {
//        return marcajeRepository.findByUsuario(usuario);
//    }
//
//    public List<Marcaje> obtenerTodosLosMarcajes() {
//        return marcajeRepository.findAll();
//    }

    @Autowired
    public MarcajeRepository marcajeRepository;

    public Marcaje registrarEntrada(Usuarios usuario) {
        Marcaje marcaje = new Marcaje();
        marcaje.setUsuario(usuario);  // Ya no usamos Optional aquí
        marcaje.setHoraEntrada(LocalDateTime.now());
        return marcajeRepository.save(marcaje);
    }

    public Marcaje registrarSalida(Usuarios usuario) {
        List<Marcaje> marcajes = marcajeRepository.findByUsuario(usuario);  // Directamente el usuario
        if (!marcajes.isEmpty()) {
            Marcaje ultimoMarcaje = marcajes.get(marcajes.size() - 1);
            if (ultimoMarcaje.getHoraSalida() == null) {
                ultimoMarcaje.setHoraSalida(LocalDateTime.now());
                return marcajeRepository.save(ultimoMarcaje);
            }
        }
        return null;  // Si no hay marcajes o ya tiene hora de salida
    }

    public List<Marcaje> obtenerMarcajes(Usuarios usuario) {
        return marcajeRepository.findByUsuario(usuario);  // Ya no usamos Optional
    }

    public Marcaje obtenerUltimoMarcaje(Usuarios usuario) {
        return marcajeRepository.findTopByUsuarioOrderByIdDesc(usuario);  // Directamente el usuario
    }

    public List<Marcaje> obtenerTodosLosMarcajes() {
        return marcajeRepository.findAll();
    }

    public Page<Marcaje> obtenerMarcajesPaginados(Pageable pageable) {
        return marcajeRepository.findAll(pageable);
    }
}
