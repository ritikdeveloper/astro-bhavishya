package com.astro.AstroRitaChaturvedi.Convertor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESEncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "1234567890123456"; // Use a secure key in production

    public static String encrypt(String value) {
        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedValue = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }
}
