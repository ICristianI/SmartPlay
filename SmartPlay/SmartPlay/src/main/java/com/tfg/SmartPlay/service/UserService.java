package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserComponent userComponent;

    /**
     * Valida si un correo o nombre de usuario ya están en uso.
     */
    public boolean validarUsuarioYCorreo(User user, Model model, boolean isEditing) {
        Optional<User> usuarioExistente = userRepository.findByEmail(user.getEmail());
        Optional<User> nombreExistente = userRepository.findByNombre(user.getNombre());

        if (usuarioExistente.isPresent() && (!isEditing || !usuarioExistente.get().getId().equals(user.getId()))) {
            model.addAttribute("error", "El correo electrónico ya está en uso.");
            return false;
        }
        if (nombreExistente.isPresent() && (!isEditing || !nombreExistente.get().getId().equals(user.getId()))) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return false;
        }
        return true;
    }

    public void deleteUser(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            userComponent.logout(request, response);
        } else {
            throw new Exception("Usuario no encontrado.");
        }
    }

    @Autowired
    private VerificationTokenService tokenService;

    public void registerUser(User user) {
        user.setEnabled(false); // No activado hasta que verifique su correo
        userRepository.save(user);
        
        tokenService.sendVerificationEmail(user);
    }
}
