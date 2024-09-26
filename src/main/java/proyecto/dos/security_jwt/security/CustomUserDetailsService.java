package proyecto.dos.security_jwt.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import proyecto.dos.security_jwt.models.Roles;
import proyecto.dos.security_jwt.models.Usuarios;
import proyecto.dos.security_jwt.repositories.UsuariosRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Data
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UsuariosRepository usuariosRepository;

    @Autowired
    public CustomUserDetailsService (UsuariosRepository usuariosRepository){
        this.usuariosRepository = usuariosRepository;
    }

    //Sirve para traer la lista de autoridades
    public Collection<GrantedAuthority> mapToAutorities(List<Roles> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    //Sirve para traer el usuario con sus datos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios usuario= usuariosRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuario.getUsername(), usuario.getPassword(), mapToAutorities(usuario.getRoles()));
    }
}
