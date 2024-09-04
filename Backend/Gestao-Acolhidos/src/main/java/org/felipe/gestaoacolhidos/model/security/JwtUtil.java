package org.felipe.gestaoacolhidos.model.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.HttpServletRequest;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${api.private.key}")
    private String jwtSecret;  // Ler o segredo do arquivo de propriedades

    @Value("${api.issuer.name}")
    private String jwtIssuer;  // Ler o emissor do arquivo de propriedades

    private long EXPIRADE_TIME = 86400000;// 24 hours

    private SecretKey getJwtSecretKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }

        // Decode the Base64 encoded key
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

        // Create a SecretKeySpec using the decoded key bytes
        return new SecretKeySpec(keyBytes, "HmacSHA512"); // Ensure the algorithm matches your signing algorithm
    }
    public String generateToken(String email, Role role) {
        SecretKey key = getJwtSecretKey();
        if (key == null) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        try {
            // Create the signing key
            JWSSigner signer = new MACSigner(key.getEncoded());

            // Prepare JWT with claims
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .claim("role", role.name())
                    .issuer(jwtIssuer)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRADE_TIME))
                    .build();

            // Create JWS object with the specified header and claims
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS512),
                    claimsSet
            );

            // Sign the JWS object
            signedJWT.sign(signer);

            // Serialize to compact form
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new RuntimeException("Error while generating JWT", e);
        }
    }

    public String getEmailFromToken(String token) {
        try {
            // Create the verifier
            SecretKey key = getJwtSecretKey();
            MACVerifier verifier = new MACVerifier(key.getEncoded());

            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Verify the signature
            if (!signedJWT.verify(verifier)) {
                throw new IllegalArgumentException("Invalid JWT signature");
            }

            // Extract the claims
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // Get the subject (email)
            return claimsSet.getSubject();

        } catch (Exception e) {
            throw new RuntimeException("Error while extracting email from JWT", e);
        }
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
            // Analisar o token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Criar o verificador de assinatura
            JWSVerifier verifier = new MACVerifier(getJwtSecretKey().getEncoded());

            // Verificar a assinatura
            if (!signedJWT.verify(verifier)) {
                return false; // Assinatura inválida
            }

            // Obter as reivindicações (claims) do token
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expiration = claims.getExpirationTime();
            if (expiration != null && expiration.toInstant().isBefore(Instant.now())) {
                // Token expirado
                return false;
            }

            // Se chegou aqui, o token é válido
            return true;

        } catch (ParseException e) {
            // Token não pode ser analisado
            return false;
        } catch (JOSEException e) {
            // Problema com a assinatura do token
            return false;
        }
    }
}