package kr.carrot.springsecurity.security.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class HashingUtils {

    public static final String SHA_256 = "SHA-256";

    public static String encryptSha256(String key) {

        try {
            MessageDigest instance = MessageDigest.getInstance(SHA_256);
            instance.update(key.getBytes(StandardCharsets.UTF_8));
            byte[] digest = instance.digest();

            return bytesToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("[hashing algorithm not found] " + e.getMessage());
        }
    }

    private static String bytesToString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
