package com.basak.dalcom.domain.accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256ConvertStrategy implements PasswordHashingStrategy {
    @Override
    public String encrypt(String plain) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(plain.getBytes());
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
