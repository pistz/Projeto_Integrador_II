package org.felipe.gestaoacolhidos.model.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${api.private.key}")
    private String jwtSecret;  // Ler o segredo do arquivo de propriedades

    @Value("${jwt.issuer}")
    private String jwtIssuer;  // Ler o emissor do arquivo de propriedades

    private long EXPIRADE_TIME = 86400000;// 24 hours

    private Key getJwtSecretKey() {
        return new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRADE_TIME))
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