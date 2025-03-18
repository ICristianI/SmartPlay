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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tfg.SmartPlay.config.VerificationFilter;

// Configuración de seguridad   

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final VerificationFilter verificationFilter;
    private final RepositoryUserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(RepositoryUserDetailsService userDetailService, VerificationFilter verificationFilter) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.verificationFilter = verificationFilter;
    }

    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    // Gestor de autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Proveedor de autenticación
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // Configuración de seguridad para web
    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()) // Deshabilita CSRF
                .authenticationProvider(authenticationProvider());

        http.addFilterBefore(verificationFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/error", "/login", "/signup", "/css/**", "/js/**", "/images/**",
                                "/users/register", "/json/**", "/config", "/msj", "/regin", "/verify",
                                "/users/verificar", "/users/verificar/**", "/users/resend")
                        .permitAll()
                        .requestMatchers("/f/**", "/fames", "/juegos", "/fichas",
                                "/verFichas", "/crearCuadernos", "/f/listarFichas", "/f/ficha/image/**",
                                "/Fichas/verFichas",
                                "/juegos/**","/ahorcado/**", "/Cuadernos", "/users/**", "/cuadernos/**")
                        .hasAnyRole("ALUMNO", "PROFESOR"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }
}
