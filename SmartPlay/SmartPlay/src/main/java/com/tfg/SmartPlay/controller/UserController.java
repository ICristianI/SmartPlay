package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.config.AuthService;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class UserController {

    

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

/* 
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
*/
    // Registro de usuario
       // Registro de usuario
    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            return "RegistrarIniciarSesion/Registrar"; // Redirigir al formulario de registro si el correo ya existe
        }
        if (userRepository.existsByNombre(user.getNombre())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "RegistrarIniciarSesion/Registrar"; // Redirigir al formulario de registro si el nombre de usuario ya existe
        }

        // Encriptar la contraseña antes de guardar
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        userRepository.save(user);

        return "redirect:/users/perfil/" + user.getId(); // Redirigir al perfil del usuario tras el registro exitoso
    }
/* 
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpServletResponse response,
                        Model model) {
        User user = userRepository.findByEmail(email);
    
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
    
            Cookie authCookie = new Cookie("authToken", token);
            authCookie.setHttpOnly(true);
            authCookie.setPath("/");
            response.addCookie(authCookie);
    
            return "redirect:/users/perfil/" + user.getId();
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "RegistrarIniciarSesion/IniciarSesion";
        }
    }
    


    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                    @RequestParam("password") String password,
                    HttpServletResponse response, 
                    Model model) {
    User user = userRepository.findByEmail(email);

    if (user != null && passwordEncoder.matches(password, user.getPassword())) {
        String token = authService.authenticateUser(user.getEmail());

        Cookie authCookie = new Cookie("authToken", token);
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        response.addCookie(authCookie);

        return "redirect:/users/" + user.getId() + "?loggedIn=true";
    } else {
        model.addAttribute("error", "Credenciales inválidas");
        return "RegistrarIniciarSesion/IniciarSesion";
    }
}
    */
    @GetMapping("/login")
    public String loginPage(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpServletResponse response,
                            Model model) {
        User user = userRepository.findByEmail(email);
    
        if (user != null && user.getPassword().equals(password)) {
            // Redirigir al perfil del usuario si las credenciales son válidas
            return "redirect:/users/perfil/" + user.getId();
        } else {
            // Mostrar un mensaje de error si las credenciales no son válidas
            model.addAttribute("error", "Credenciales inválidas");
            return "RegistrarIniciarSesion/IniciarSesion";
        }
    }



    @GetMapping("/perfil/{id}")
    public String userDashboard(@PathVariable Long id, Model model) {
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
    
        if (!authenticatedUser.getId().equals(id)) {
            throw new AccessDeniedException("No tienes acceso a este perfil.");
        }
        */
    
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("user", user);
    
        return "PaginaUsuario/Perfil";
    }
    

    @PostMapping("/{id}/upload")
    public String uploadProfilePhoto(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String fileName = image.getOriginalFilename();
        // Cambia la ruta de almacenamiento a un directorio externo
        Path uploadPath = Paths.get("uploads/userImages");

        try {

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // Crea la carpeta si no existe
            }

            Files.copy(image.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            // Establece la URL relativa que será manejada por el ResourceHandler
            user.setPhoto("/uploads/userImages/" + fileName);
            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen", e);
            
        }

        return "redirect:/users/perfil/" + id;
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(@PathVariable Long id,MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("error", "El archivo excede el tamaño máximo permitido.");
        return "redirect:/users/perfil/" + id; // Redirige a una página de error o muestra el mensaje en la misma página
    }

    public User crearUsuario(User user) {
        return userRepository.save(user);
    }

    public List<User> listarUsuariosPorRol(User.Rol rol) {
        return userRepository.findByRol(rol);
    }

    public User encontrarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
