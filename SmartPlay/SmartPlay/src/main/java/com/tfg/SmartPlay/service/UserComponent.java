package com.tfg.SmartPlay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserComponent {

    @Autowired
    UserRepository userRepository;

    public boolean isLoggedUser() {
        return getUser().isPresent();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if (!isAdmin()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    public Optional<User> getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByEmail(username);
    }

    public boolean isAdmin() {
        Optional<User> opUser = getUser();
        if (opUser.isEmpty()) {
            return false;
        } else {
            return opUser.get().getRoles().contains("ADMIN");
        }
    }
}
