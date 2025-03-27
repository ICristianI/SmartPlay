
package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoSopaLetrasRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class JuegoSopaLetrasService {

    @Autowired
    private JuegoSopaLetrasRepository juegoSopaLetrasRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    public void guardarJuego(JuegoSopaLetras juego, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        juego.setUsuario(usuario);
        juegoSopaLetrasRepository.save(juego);
    }

    public Optional<JuegoSopaLetras> obtenerJuego(Long juegoId, String email) {
        return juegoSopaLetrasRepository.findById(juegoId);
    }

    public void editarJuego(Long juegoId, JuegoSopaLetras juegoEditado, String email) {
        JuegoSopaLetras juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setAsignatura(juegoEditado.getAsignatura());
        juego.setContenido(juegoEditado.getContenido());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setPrivada(juegoEditado.isPrivada());
        juego.setPalabras(juegoEditado.getPalabras());

        juegoSopaLetrasRepository.save(juego);
    }

    public Page<Cuaderno> obtenerCuadernosConJuegoPaginados(Juego juego, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.obtenerCuadernosPorJuego(juego, pageable);
    }

    public Page<JuegoSopaLetras> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return juegoSopaLetrasRepository.findByUsuario(usuario, pageable);
    }
}
