package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            return "RegistrarIniciarSesion/Registrar"; // Vuelve al formulario con el mensaje de error
        }
        if (userRepository.existsByNombre(user.getNombre())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "RegistrarIniciarSesion/Registrar"; // Vuelve al formulario con el mensaje de error
        }

        User savedUser = userRepository.save(user);

        return "redirect:/users/" + savedUser.getId();

    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            model.addAttribute("nombre", user.getNombre());
            return "redirect:/users/" + user.getId();
        } else if (user == null) {
            model.addAttribute("error", "Correo incorrecto");
            return "login";
        } else {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }
    }

    @GetMapping("/{id}")
    public String userDashboard(@PathVariable Long id, Model model) {
        // Buscar el usuario por ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Añadir el usuario al modelo para usarlo en la plantilla
        model.addAttribute("user", user);

        // Mostrar la página personalizada
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

        return "redirect:/users/" + id;
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(@PathVariable Long id,MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("error", "El archivo excede el tamaño máximo permitido.");
        return "redirect:/users/" + id; // Redirige a una página de error o muestra el mensaje en la misma página
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
