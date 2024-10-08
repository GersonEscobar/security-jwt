package proyecto.dos.security_jwt.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proyecto.dos.security_jwt.models.Usuarios;
import proyecto.dos.security_jwt.services.UsuarioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {"http://localhost:4200", "http://18.219.196.79"})
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public List<Usuarios> obtenerTodosLosUsuarios(){
        return  usuarioService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/paginados/")
    public Page<Usuarios> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioService.obtenerUsuariosPaginados(pageable);
    }

    @GetMapping("/{username}")
    public Optional<Usuarios> obtenerUsuario(@PathVariable("username") String username){
        return  usuarioService.obtenerUsuario(username);
    }

    @GetMapping("/id/{usuarioId}")
    public ResponseEntity<Optional<Usuarios>> obtenerUsuarioPorId(@PathVariable("usuarioId") Long usuarioId) {
        try {
            Optional<Usuarios> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Usuarios> actualizarUsuario(
            @PathVariable("usuarioId") Long usuarioId,
            @RequestBody Usuarios usuarioActualizado) {
        try {
            Usuarios usuarioActualizadoEnDb = usuarioService.actualizarUsuario(usuarioId, usuarioActualizado);
            return new ResponseEntity<>(usuarioActualizadoEnDb, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{usuarioId}")
    public void eliminarUsuario(@PathVariable("usuarioId") Long usuarioId){
        usuarioService.eliminarUsiario(usuarioId);
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<String> eliminarUsuarioPorUsername(@PathVariable("username") String username) {
        try {
            usuarioService.eliminarUsuarioPorUsername(username);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

}
