package proyecto.dos.security_jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proyecto.dos.security_jwt.models.Marcaje;
import proyecto.dos.security_jwt.models.Usuarios;
import proyecto.dos.security_jwt.services.MarcajeService;
import proyecto.dos.security_jwt.services.UsuarioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marcajes")
public class MarcajeController {

    @Autowired
    public MarcajeService marcajeService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/entrada/{username}")
    public ResponseEntity<?> registrarEntrada(@PathVariable String username) {
        Optional<Usuarios> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {
            // Extraemos el usuario del Optional
            Usuarios usuario = usuarioOptional.get();
            Marcaje marcaje = marcajeService.registrarEntrada(usuario);
            return new ResponseEntity<>(marcaje, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/salida/{username}")
    public ResponseEntity<?> registrarSalida(@PathVariable String username) {
        Optional<Usuarios> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {  // Usa isPresent() en lugar de comparar con null
            Usuarios usuario = usuarioOptional.get();  // Extrae el usuario del Optional
            Marcaje marcaje = marcajeService.registrarSalida(usuario);
            return new ResponseEntity<>(marcaje, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/historial/{username}")
    public ResponseEntity<?> obtenerMarcajes(@PathVariable String username) {
        Optional<Usuarios> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {
            Usuarios usuario = usuarioOptional.get();
            List<Marcaje> marcajes = marcajeService.obtenerMarcajes(usuario);
            return new ResponseEntity<>(marcajes, HttpStatus.OK);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public List<Marcaje> obtenerTodosLosMarcajes(){
        return marcajeService.obtenerTodosLosMarcajes();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ultimo/{username}")
    public ResponseEntity<?> obtenerUltimoMarcaje(@PathVariable String username) {
        Optional<Usuarios> usuarioOptional = usuarioService.obtenerUsuario(username);
        if (usuarioOptional.isPresent()) {
            Usuarios usuario = usuarioOptional.get();
            Marcaje ultimoMarcaje = marcajeService.obtenerUltimoMarcaje(usuario);
            return new ResponseEntity<>(ultimoMarcaje, HttpStatus.OK);
        }
        return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginados/")
    public Page<Marcaje> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return marcajeService.obtenerMarcajesPaginados(pageable);
    }
}
