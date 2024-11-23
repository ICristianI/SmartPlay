package com.tfg.SmartPlay.restController;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.controller.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    // Endpoint para crear un nuevo usuario
    @PostMapping
    public User crearUsuario(@RequestBody User user) {
        return userService.crearUsuario(user);
    }

    // Endpoint para listar usuarios por rol
    @GetMapping("/role/{role}")
    public List<User> listarUsuariosPorRol(@PathVariable("role") User.Rol role) {
        return userService.listarUsuariosPorRol(role);
    }

    // Endpoint para obtener un usuario por email
    @GetMapping("/{email}")
    public User encontrarPorEmail(@PathVariable("email") String email) {
        return userService.encontrarPorEmail(email);
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping
    public List<User> obtenerTodosLosUsuarios() {
        return userService.listarUsuariosPorRol(null); // Si tienes un método en `UserService` para listar todos, úsalo aquí.
    }
}
