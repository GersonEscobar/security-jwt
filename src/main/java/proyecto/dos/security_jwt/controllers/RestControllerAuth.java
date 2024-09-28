package proyecto.dos.security_jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import proyecto.dos.security_jwt.dtos.DtoAuthRespuesta;
import proyecto.dos.security_jwt.dtos.DtoLogin;
import proyecto.dos.security_jwt.dtos.DtoRegistro;
import proyecto.dos.security_jwt.models.Roles;
import proyecto.dos.security_jwt.models.Usuarios;
import proyecto.dos.security_jwt.repositories.RolRepository;
import proyecto.dos.security_jwt.repositories.UsuariosRepository;
import proyecto.dos.security_jwt.security.JwtGenerador;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class RestControllerAuth {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private RolRepository rolRepository;
    private UsuariosRepository usuariosRepository;
    private JwtGenerador jwtGenerador;

    @Autowired
    public RestControllerAuth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RolRepository rolRepository, UsuariosRepository usuariosRepository, JwtGenerador jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.usuariosRepository = usuariosRepository;
        this.jwtGenerador = jwtGenerador;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody DtoRegistro dtoRegistro) {
        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())) {
            return new ResponseEntity<>("El usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }

        Usuarios usuarios = new Usuarios();
        usuarios.setUsername(dtoRegistro.getUsername());
        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuarios.setNombre(dtoRegistro.getNombre());
        usuarios.setApellido(dtoRegistro.getApellido());
        usuarios.setEmail(dtoRegistro.getEmail());
        usuarios.setTelefono(dtoRegistro.getTelefono());
        usuarios.setPerfil(dtoRegistro.getPerfil());

        Roles roles = rolRepository.findByName("USER").get();
        usuarios.setRoles(Collections.singletonList(roles));
        try {
            usuariosRepository.save(usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al guardar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>("Registro de usuario exitoso", HttpStatus.OK);
    }



    @PostMapping("/registerAdmin")
    public ResponseEntity<String>  registrarAdmin(@RequestBody DtoRegistro dtoRegistro){
        if(usuariosRepository.existsByUsername(dtoRegistro.getUsername())){
            return new ResponseEntity<>("El usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        Usuarios usuarios = new Usuarios();
        usuarios.setUsername(dtoRegistro.getUsername());
        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuarios.setNombre(dtoRegistro.getNombre());
        usuarios.setApellido(dtoRegistro.getApellido());
        usuarios.setEmail(dtoRegistro.getEmail());
        usuarios.setTelefono(dtoRegistro.getTelefono());
        usuarios.setPerfil(dtoRegistro.getPerfil());

        Roles roles = rolRepository.findByName("ADMIN").get();
        usuarios.setRoles(Collections.singletonList(roles));
        usuariosRepository.save(usuarios);
        return new ResponseEntity<>("Registro de admin exitoso", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<DtoAuthRespuesta> login(@RequestBody DtoLogin dtoLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoLogin.getUsername(), dtoLogin.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar el token incluyendo roles
            String token = jwtGenerador.generarToken(authentication);

            return new ResponseEntity<>(new DtoAuthRespuesta(token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new DtoAuthRespuesta("Credenciales incorrectas"), HttpStatus.UNAUTHORIZED);
        }
    }

}
