package org.orange.oie.internship2025.conferenceroombooking.jwt.util;


import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RsaKeyLoader {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
            try {
                privateKey = RsaKey.getPrivateKey("src/main/resources/keys/private_key.pem");
                publicKey = RsaKey.getPublicKey("src/main/resources/keys/public_key.pem");
            } catch (Exception e) {
                throw new RuntimeException("Failed to load RSA keys", e);
            }
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }
    }