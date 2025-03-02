package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.UserComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService;

    // Maneja el formulario de registro

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam String rol, Model model) {
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            return "RegistrarIniciarSesion/Registrar"; // Redirigir al formulario de registro si el correo ya existe
        }
        if (userRepository.existsByNombre(user.getNombre())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return "RegistrarIniciarSesion/Registrar"; // Redirigir al formulario de registro si el nombre de usuario ya existe
                                                       
        }

        // Si no se especifica un rol, se establece como ALUMNO
        if (rol == null || rol.isEmpty()) {
            user.setRoles(List.of("ALUMNO"));
        } else {
            user.setRoles(List.of(rol));
        }

        Blob photo;
        try {
            photo = imagenService.getDefaultProfileImage();
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener la imagen de perfil predeterminada.");
            return "RegistrarIniciarSesion/Registrar";
        }
        String encode = passwordEncoder.encode(user.getPassword());

        user.setPassword(encode);
        user.setPhoto(photo);
        userRepository.save(user);

        return "redirect:/login";

    }

    // Muestra el perfil del usuario

    @GetMapping("/perfil")
    public String profile(Model model) {
        model.addAttribute("user", userComponent.getUser().get());
        return "PaginaUsuario/Perfil";
    }

    // Muestra la imagen de perfil del usuario
    @GetMapping("/image")
    public ResponseEntity<Object> downloadImage(Model model) throws SQLException {
        Optional<User> op = userRepository.findById(userComponent.getUser().get().getId());
        ResponseEntity<Object> imageResponse = imagenService.getImageResponse(op.get().getPhoto());
        return imageResponse;
    }

    // Sirve para evitar que se intente mapear un multipart a Blob
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("photo");
    }

    // Maneja el cambio de la imagen de perfil del usuario
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String guardarFotoUser(@RequestParam("photo") MultipartFile photo, Model model) throws Exception {
        Blob photoBlob = imagenService.saveImage(photo);
        User user = userComponent.getUser().get();
        user.setPhoto(photoBlob);
        userRepository.save(user);
        return "redirect:/users/perfil";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(@PathVariable Long id, MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("error", "El archivo excede el tamaño máximo permitido.");
        return "redirect:/users/perfil"; 
    }

}
