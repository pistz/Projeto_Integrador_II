package org.felipe.gestaoacolhidos.model.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${api.private.key}")
    private String jwtSecret;  // Ler o segredo do arquivo de propriedades

    @Value("${api.issuer.name}")
    private String jwtIssuer;  // Ler o emissor do arquivo de propriedades

    private long EXPIRADE_TIME = 86400000;// 24 hours

    private SecretKey getJwtSecretKey() {
        // Create a secure key using the base64-encoded key
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuer(jwtIssuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRADE_TIME))
                .signWith(SignatureAlgorithm.HS512, getJwtSecretKey())
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getJwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getJwtSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token has expired
            return false;
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // Unsupported JWT
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // Malformed JWT
            return false;
        } catch (io.jsonwebtoken.SignatureException e) {
            // Invalid signature
            return false;
        } catch (Exception e) {
            // Other exceptions
            return false;
        }
    }
}