//package org.felipe.gestaoacolhidos.model.keyGen;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//public class KeyGenerator {
//    public static void main(String[] args) {
//        // Generate a secure key for HS512
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//
//        // Encode the key in base64 format
//        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//
//        // Print the base64-encoded key
//        System.out.println("Base64-encoded key: " + base64Key);
//    }
//}