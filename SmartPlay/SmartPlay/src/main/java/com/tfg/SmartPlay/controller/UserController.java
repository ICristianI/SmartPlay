package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    
    @GetMapping("/signup")
    public String signup(Model model) {
        return "RegistrarIniciarSesion/Registrar";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,Model model) {

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            return "RegistrarIniciarSesion/Registrar";  // Vuelve al formulario con el mensaje de error
        }
        if (userRepository.existsByNombre(user.getNombre())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "RegistrarIniciarSesion/Registrar";  // Vuelve al formulario con el mensaje de error
        }
  
        User savedUser = userRepository.save(user);

        return "redirect:/users/" + savedUser.getId();

    }

    @GetMapping("/{id}")
    public String userDashboard(@PathVariable Long id, Model model) {
    // Buscar el usuario por ID
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    // Añadir el usuario al modelo para usarlo en la plantilla
    model.addAttribute("user", user);

    // Mostrar la página personalizada
    return "PaginaUsuario/Perfil";
    }


@PostMapping("/{id}/upload")
public String uploadProfilePhoto(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    // Guardar la imagen en un directorio local
    String fileName = id + "_" + image.getOriginalFilename();
    Path uploadPath = Paths.get("uploads");
    try {
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Files.copy(image.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        user.setPhoto("/uploads/" + fileName); // Ruta accesible para la imagen
        userRepository.save(user);
    } catch (IOException e) {
        throw new RuntimeException("Error al subir la imagen", e);
    }

    return "redirect:/users/" + id;
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
