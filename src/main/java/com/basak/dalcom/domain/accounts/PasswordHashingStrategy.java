package com.basak.dalcom.domain.accounts;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public interface PasswordHashingStrategy {
    String encrypt(String plain) throws NoSuchAlgorithmException;
}
