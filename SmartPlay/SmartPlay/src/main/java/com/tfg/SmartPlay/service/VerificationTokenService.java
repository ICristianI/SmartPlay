package com.tfg.SmartPlay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.entity.VerificationToken;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {
    
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private UserRepository userRepository;

    public void sendVerificationEmail(User user) {
        Optional<VerificationToken> existingToken = tokenRepository.findByUser(user);


if (existingToken.isPresent()) {
    System.out.println("Token encontrado: " + existingToken.get().getId());
    
    tokenRepository.delete(existingToken.get());
    tokenRepository.flush(); // Forzar la eliminación inmediata

    System.out.println("Intento de eliminación realizado.");
} else {
    System.out.println("No se encontró token para eliminar.");
}
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        
        tokenRepository.save(verificationToken);
        
        String verificationLink = "http://localhost:8080/users/verificar?token=" + token;
        String message = "Haz clic en el siguiente enlace para verificar tu cuenta: " + verificationLink;
        
        sendEmail(user.getEmail(), "Verifica tu cuenta", message);
    }
    public void verifyToken(String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();
            if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Token expirado");
            }
            
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            
            tokenRepository.delete(verificationToken);
        } else {
            throw new RuntimeException("Token inválido");
        }
    }

    

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
    }

    
}

