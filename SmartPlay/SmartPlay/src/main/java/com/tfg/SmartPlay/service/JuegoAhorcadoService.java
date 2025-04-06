package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoAhorcadoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@Service
public class JuegoAhorcadoService {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoAhorcadoRepository juegoAhorcadoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private ImagenService imagenService;

    public List<JuegoAhorcado> listarJuegos(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return juegoAhorcadoRepository.findByUsuario(usuario);
    }

    public void editarJuego(Long juegoId, JuegoAhorcado juegoEditado, String email) {
        JuegoAhorcado juego = (JuegoAhorcado) juegoService.obtenerJuego(juegoId, email)
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

public void guardarJuego(JuegoAhorcado juego, String email, MultipartFile imagenJuego) {
    User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    juego.setUsuario(usuario);

    try {
        if (imagenJuego != null && !imagenJuego.isEmpty()) {
            Blob imagen = imagenService.saveImage(imagenJuego);
            juego.setImagen(imagen);
        } else {

            InputStream defaultImgStream = getClass().getResourceAsStream("/static/images/generalImages/CREARAHORCADO.png");
            if (defaultImgStream != null) {
                byte[] bytes = defaultImgStream.readAllBytes();
                Blob defaultBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
                juego.setImagen(defaultBlob);
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar la imagen del juego", e);
    }

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

    public Optional<JuegoAhorcado> obtenerJuego(Long juegoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<JuegoAhorcado> juego = juegoAhorcadoRepository.findById(juegoId);
        return juego.filter(j -> j.getUsuario().getId().equals(usuario.getId()));
    }



}
