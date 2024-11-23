package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Crear un nuevo usuario
    public User crearUsuario(User user) {
        return userRepository.save(user);
    }

    // Listar usuarios por rol
    public List<User> listarUsuariosPorRol(User.Rol rol) {
        return userRepository.findByRol(rol);
    }

    // Encontrar usuario por email
    public User encontrarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
