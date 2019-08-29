package app.utils;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionUtil {

    private Logger logger;

    public EncryptionUtil(Logger logger) {
        this.logger = logger;
    }

    public String encode(CharSequence sequence) {
        String plainTextPassword = (String) sequence;
        if (plainTextPassword == null) {
            return null;
        }

        String algorithm = "SHA";
        byte[] plainText = plainTextPassword.getBytes();

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Password encrypt exception. Message: {}", e.getMessage());
            return null;
        }

        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();
        StringBuilder encryptedPassword = new StringBuilder();

        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                encryptedPassword.append("0");
            }

            encryptedPassword.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return encryptedPassword.toString();
    }
}
