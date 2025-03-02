/*package com.tfg.SmartPlay;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear un usuario ALUMNO
        User alumno = new User(
            null,  // El ID se genera automáticamente
            "Alumno",
            20,
            "alumno@example.com",
            passwordEncoder.encode("123"), // Contraseña cifrada
            List.of("ALUMNO"),
            null,  // Se puede dejar como null si no se quiere agregar foto
            null   // Puede dejarse null si no hay fichas asociadas
        );

        // Crear un usuario PROFESOR
        User profesor = new User(
            null,  // El ID se genera automáticamente
            "Profesor",
            35,
            "profesor@example.com",
            passwordEncoder.encode("123"), // Contraseña cifrada
            List.of("PROFESOR"),
            null,  // Se puede dejar como null si no se quiere agregar foto
            null   // Puede dejarse null si no hay fichas asociadas
        );

        // Guardar los usuarios en la base de datos
        userRepository.save(alumno);
        userRepository.save(profesor);
    }
}
*/