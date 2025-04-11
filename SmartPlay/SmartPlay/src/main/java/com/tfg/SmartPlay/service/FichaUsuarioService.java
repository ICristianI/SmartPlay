package com.tfg.SmartPlay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaUsuario;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.FichaUsuarioRepository;

@Service
public class FichaUsuarioService {

    @Autowired
    private FichaUsuarioRepository fichaUsuarioRepository;

    @Autowired
    private FichaRepository fichaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Guarda nota y respuestas del usuario para una ficha específica.
     */
    public void guardarNotaYRespuestas(Long fichaId, Optional<User> user, Double nota, Object respuestasRaw) {
        Ficha ficha = fichaRepository.findById(fichaId).orElseThrow();
        FichaUsuario fichaUsuario = fichaUsuarioRepository.findByFichaAndUsuario(ficha, user)
                .orElseGet(() -> {
                    FichaUsuario nuevo = new FichaUsuario();
                    nuevo.setFicha(ficha);
                    nuevo.setUsuario(user.orElse(null));
                    nuevo.setIntentos(0);
                    return nuevo;
                });

        fichaUsuario.setNota(nota);
        fichaUsuario.setIntentos(fichaUsuario.getIntentos() + 1);

        try {
            String respuestasJson = objectMapper.writeValueAsString(respuestasRaw);
            fichaUsuario.setRespuestas(respuestasJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar las respuestas del usuario", e);
        }

        fichaUsuarioRepository.save(fichaUsuario);
    }

    public Optional<FichaUsuario> obtenerNota(Long fichaId, Long userId) {
        return fichaUsuarioRepository.findByFichaIdAndUsuarioId(fichaId, userId);
    }
}
