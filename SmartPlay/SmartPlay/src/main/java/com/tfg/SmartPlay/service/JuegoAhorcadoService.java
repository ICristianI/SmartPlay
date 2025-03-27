package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoAhorcadoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoAhorcadoService {

    @Autowired
    private JuegoAhorcadoRepository juegoAhorcadoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    public List<JuegoAhorcado> listarJuegos(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return juegoAhorcadoRepository.findByUsuario(usuario);
    }

    public Optional<JuegoAhorcado> obtenerJuego(Long juegoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<JuegoAhorcado> juego = juegoAhorcadoRepository.findById(juegoId);
        return juego.filter(j -> j.getUsuario().getId().equals(usuario.getId()));
    }

    public void editarJuego(Long juegoId, JuegoAhorcado juegoEditado, String email) {
        JuegoAhorcado juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setAsignatura(juegoEditado.getAsignatura());
        juego.setContenido(juegoEditado.getContenido());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setPalabra(juegoEditado.getPalabra());
        juego.setPrivada(juegoEditado.isPrivada());
        juego.setMaxIntentos(juegoEditado.getMaxIntentos());

        juegoAhorcadoRepository.save(juego);
    }

    public void guardarJuego(JuegoAhorcado juego, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        juego.setUsuario(usuario);
        juegoAhorcadoRepository.save(juego);
    }


    public Page<JuegoAhorcado> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return juegoAhorcadoRepository.findByUsuario(usuario, pageable);
    }

    public Page<Cuaderno> obtenerCuadernosConJuegoPaginados(Juego juego, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return cuadernoRepository.obtenerCuadernosPorJuego(juego, pageable);
}

}
