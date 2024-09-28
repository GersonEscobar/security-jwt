package proyecto.dos.security_jwt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proyecto.dos.security_jwt.models.Usuarios;
import proyecto.dos.security_jwt.repositories.UsuariosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuariosRepository usuariosRepository;

    public List<Usuarios> obtenerTodosLosUsuarios() {
        return usuariosRepository.findAll();
    }

    public Optional<Usuarios> obtenerUsuario(String username) {
        return usuariosRepository.findByUsername(username);
    }

    public Optional<Usuarios> obtenerUsuarioPorId(Long usuarioId) {
        Optional<Usuarios> usuarioOptional = usuariosRepository.findById(usuarioId);
        if (usuarioOptional.isPresent()) {
            return Optional.of(usuarioOptional.get());
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
    }

    public void eliminarUsiario(Long usuarioId) {
        usuariosRepository.deleteById(usuarioId);
    }

    public Page<Usuarios> obtenerUsuariosPaginados(Pageable pageable) {
        return usuariosRepository.findAll(pageable);
    }

    public Optional<Usuarios> guardarUsuario(Usuarios usuario){
        Optional<Usuarios> usuarioLocal = usuariosRepository.findByUsername(usuario.getUsername());
        if(usuarioLocal.isPresent()){
            throw new  RuntimeException("El usuario ya esta presente");
        }
        else {
            usuarioLocal = Optional.of(usuariosRepository.save(usuario));
        }
        return usuarioLocal;
    }
}
