package com.basak.dalcom.domain.accounts;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SHA256ConvertStrategyTest {
    private final PasswordHashingStrategy strategy = new SHA256ConvertStrategy();

    @Test
    public void encryptTest() {
        // Given
        String password1 = "this_is_password_123!";

        // Then
        assertDoesNotThrow(() -> {
            // When
            strategy.encrypt(password1);
        });
    }

    @Test
    public void hashResultEqualityTest() throws NoSuchAlgorithmException {
        String password1 = "this_is_password_123!";
        String password2 = "this_is_password_123!";
        String password3 = "this_is_password_123?";

        String hashResult1 = strategy.encrypt(password1);
        String hashResult2 = strategy.encrypt(password2);
        String hashResult3 = strategy.encrypt(password3);

        assertThat(hashResult1).isEqualTo(hashResult2);
        assertThat(hashResult1).isNotEqualTo(hashResult3);
        assertThat(hashResult2).isNotEqualTo(hashResult3);
    }
}
