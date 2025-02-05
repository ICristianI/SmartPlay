/*package com.tfg.SmartPlay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserRepository userRepository;

    // Bean para el encriptado de contraseñas
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

 
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
               .userDetailsService(userDetailsService)
               .passwordEncoder(passwordEncoder())
               .and()
               .build();
}

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            
            .userDetailsService(userDetailsService)
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signin", "/signup", "/images/**", "/css/**", "/js/**", "/json/translation.json","/users/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/signin")
                .failureUrl("/signin?error=true") // Redirige en caso de error          
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            );
        return http.build();
    }
}
*/
