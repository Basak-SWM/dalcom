package com.basak.dalcom.spring.security.config;

import com.basak.dalcom.spring.security.filter.SecurityAuthenticationFilter;
import com.basak.dalcom.spring.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityAuthenticationFilter securityAuthenticationFilter() {
        return new SecurityAuthenticationFilter(userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(corsConfigurer())
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/api/v1/accounts/login").permitAll()
            .antMatchers("/api/v1/accounts/user/signup").permitAll()
            .antMatchers("/api/v1/accounts/coach/signup").permitAll()
            .antMatchers("/api/v1/accounts/logout").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .antMatchers("/api/v1/presentations/**/speeches/**/clova-result-callback")
            .permitAll()
            .antMatchers("/**").authenticated()
//            .antMatchers("/**").permitAll() // TODO : authenticated로 막을 예정
            .and()
            .formLogin().disable();

        http.addFilterBefore(securityAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> corsConfigurer() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000/");
        configuration.addAllowedOrigin("http://localhost:8000/");
        configuration.addAllowedOrigin("https://api.tokpeanut.com/");
        configuration.addAllowedOrigin("http://api.tokpeanut.com/");
        configuration.addAllowedOrigin("http://tokpeanut.com/");
        configuration.addAllowedOrigin("https://tokpeanut.com/");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return (cors) -> {
            cors.configurationSource(source);
        };
    }
}
