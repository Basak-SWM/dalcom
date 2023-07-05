package com.basak.dalcom;

import com.basak.dalcom.domain.accounts.PasswordHashingStrategy;
import com.basak.dalcom.domain.accounts.SHA256ConvertStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public PasswordHashingStrategy passwordHashingStrategy() {
        return new SHA256ConvertStrategy();
    }
}
