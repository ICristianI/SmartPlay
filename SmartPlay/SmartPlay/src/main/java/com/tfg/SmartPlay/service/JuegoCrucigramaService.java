package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoCrucigramaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JuegoCrucigramaService {

    @Autowired
    private JuegoCrucigramaRepository juegoCrucigramaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    public void guardarJuego(JuegoCrucigrama juego, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        juego.setUsuario(usuario);
        juego.setPistas(juego.getPistas());
        juego.setRespuestas(juego.getRespuestas());
        juegoCrucigramaRepository.save(juego);
    }

    public Optional<JuegoCrucigrama> obtenerJuego(Long juegoId, String email) {
        return juegoCrucigramaRepository.findById(juegoId);
    }

    public void editarJuego(Long juegoId, JuegoCrucigrama juegoEditado, String email) {
        JuegoCrucigrama juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setAsignatura(juegoEditado.getAsignatura());
        juego.setContenido(juegoEditado.getContenido());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setPistas(juegoEditado.getPistas());
        juego.setRespuestas(juegoEditado.getRespuestas());
        juegoCrucigramaRepository.save(juego);
    }

    public void eliminarJuego(Long juegoId, String email) {
        juegoCrucigramaRepository.deleteById(juegoId);
    }

    public Page<Cuaderno> obtenerCuadernosConJuegoPaginados(JuegoCrucigrama juego, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.obtenerCuadernosPorJuego(juego, pageable);
    }

    public Page<JuegoCrucigrama> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return juegoCrucigramaRepository.findByUsuario(usuario, pageable);
    }
}
