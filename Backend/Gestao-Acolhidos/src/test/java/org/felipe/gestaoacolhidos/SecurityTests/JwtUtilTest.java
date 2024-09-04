package org.felipe.gestaoacolhidos.SecurityTests;


import jakarta.servlet.http.HttpServletRequest;
import org.felipe.gestaoacolhidos.model.domain.enums.role.Role;
import org.felipe.gestaoacolhidos.model.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    private SecretKey jwtSecretKey;  // Use SecretKey instead of String

    private String jwtIssuer = "myJwtIssuer";

    @BeforeEach
    void setUp() {
        // Configure o segredo para o teste - chave base64 genérica
        jwtSecretKey = new SecretKeySpec(Base64
                .getDecoder()
                .decode("guOz4Ur76iDUV/e80Ntcj8w4MyxL2qyVeJNxMoAHF7392QZYDfGBy1MueKnveV8zR/idlHDSY2S8Zt7wkZMixA=="),
                "HmacSHA512");

        // Definir os campos privados usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", Base64
                .getEncoder()
                .encodeToString(jwtSecretKey
                        .getEncoded()));
        ReflectionTestUtils.setField(jwtUtil, "jwtIssuer", jwtIssuer);
    }

    @Test
    void testGenerateToken() {
        String username = "testuser";
        Role role = Role.ADMIN;

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // Verifica se o token JWT começa com a assinatura padrão
    }

    @Test
    void testGetEmailFromToken() {
        String username = "testuser";
        Role role = Role.ADMIN;

        String token = jwtUtil.generateToken(username, role);

        String extractedUsername = jwtUtil.getEmailFromToken(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testResolveToken() {
        String token = "Bearer mytoken";
        when(request.getHeader("Authorization")).thenReturn(token);

        String resolvedToken = jwtUtil.resolveToken(request);

        assertEquals("mytoken", resolvedToken);
    }

    @Test
    void testResolveToken_NoBearer() {
        when(request.getHeader("Authorization")).thenReturn("mytoken");

        String resolvedToken = jwtUtil.resolveToken(request);

        assertNull(resolvedToken);
    }

    @Test
    void testResolveToken_NullToken() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String resolvedToken = jwtUtil.resolveToken(request);

        assertNull(resolvedToken);
    }

    @Test
    void testValidateToken_ValidToken() {
        String username = "testuser";
        Role role = Role.ADMIN;

        String token = jwtUtil.generateToken(username, role);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        String username = "testuser";
        Role role = Role.ADMIN;

        // Criar um token com tempo de expiração passado
        ReflectionTestUtils.setField(jwtUtil, "EXPIRADE_TIME", -1000);  // -1000 ms para expirar imediatamente
        String token = jwtUtil.generateToken(username, role);

        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateToken_MalformedToken() {
        String malformedToken = "malformed.token.string";

        assertFalse(jwtUtil.validateToken(malformedToken));
    }

    @Test
    void testValidateToken_InvalidSignatureToken() {
        String username = "testuser";
        Role role = Role.SECRETARY;

        String token = jwtUtil.generateToken(username, role);

        // Manipular o token para ter uma assinatura inválida
        String invalidToken = token.substring(0, token.length() - 1) + "x";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void testGetJwtSecretKey_NotConfigured() {
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            jwtUtil.generateToken("username", Role.ADMIN);
        });

        assertEquals("JWT secret key is not configured", exception.getMessage());
    }
}