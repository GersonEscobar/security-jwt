package proyecto.dos.security_jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtGenerador {

    //Para crear el token por medio de la autenticacion
//    public String generarToken(Authentication authentication){
//        String username = authentication.getName();
//        Date tiempoActual = new Date();
//        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_WXPIRATION_TOKEN);
//
//        //Para generar el token
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(expiracionToken)
//                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
//                .compact();
//        return  token;
//    }

    // segunda
//    public String generarToken(Authentication authentication) {
//        String username = authentication.getName();
//
//        // Extraer roles del usuario autenticado
//        List<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority) // Convertir a string
//                .collect(Collectors.toList());
//
//        Date tiempoActual = new Date();
//        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_WXPIRATION_TOKEN);
//
//        // Generar el token
//        String token = Jwts.builder()
//                .setSubject(username)
//                .claim("roles", roles) // Incluir roles en el token
//                .setIssuedAt(tiempoActual)
//                .setExpiration(expiracionToken)
//                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
//                .compact();
//
//        return token;
//    }

    public String generarToken(Authentication authentication) {
        String username = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_WXPIRATION_TOKEN);

        // Obtener los roles del usuario
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Generar el token
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Agregar roles al token
                .setIssuedAt(tiempoActual)
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
    }



    public String obtenerUsernameDeJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token) // Cambia parseClaimsJwt a parseClaimsJws
                .getBody();
        return claims.getSubject();
    }


    public Boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt ha expirado o está incorrecto");
        }
    }

    public String obtenerRolesDeJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token).getBody();
        return claims.get("roles", String.class);
    }



}

