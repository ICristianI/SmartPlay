package com.tfg.SmartPlay.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RepositoryUserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(RepositoryUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()) // Deshabilita CSRF
                .authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/error", "/login", "/signup", "/css/**", "/js/**", "/images/**",
                        "/users/register", "/config")
                .permitAll()
                .requestMatchers("/fames", "/juegos", "/fichas", "/crearFichas", "/users/**", "/f/*")
                .hasAnyRole("ALUMNO", "PROFESOR"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/",true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }
}
