package org.delarosa.app.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.delarosa.app.usuario.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "2C8F5B12A9D34F6E7C8D5A12F0B4E9C6D1E3A7B9F0C5E8A7B2D9C4F6E1A3D5B";

    public String getToken(UserDetails user) {
        Map<String,Object> extraClaims = new HashMap<>();

        if (user instanceof Usuario) {
            var customUser = (Usuario) user;
            extraClaims.put("id", customUser.getIdUsuario());
            List<String> roles = customUser.getRoles().stream()
                    .map(rol -> rol.getNombre().name())
                    .toList();
            extraClaims.put("roles", roles);
        }
        return getToken(extraClaims, user);
    }

    // Este método privado ya estaba perfecto, no necesita cambios.
    private String getToken(Map<String,Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24)) 
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaimByToken(token,Claims::getSubject);
    }

    // <--- AÑADIDO: Método conveniente para leer el ID del token
    // Asumimos que el ID es un Integer. Si es Long, cambia Integer.class a Long.class
    public Integer getUserIdFromToken(String token) {
        return getClaimByToken(token, claims -> claims.get("id", Integer.class));
    }


    public boolean isTokenValid(String token, UserDetails user) {
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T getClaimByToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimByToken(token,Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }


    // JwtService
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}