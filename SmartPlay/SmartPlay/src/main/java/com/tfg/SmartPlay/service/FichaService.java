package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Servicio para gestionar las fichas de un usuario.

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private CuadernoRepository cuadernoRepository;
    
    // Devuelve todas las fichas de un usuario.

    public List<Ficha> listarFichas(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return fichaRepository.findByUsuario(usuario);
    }

    // Devuelve una ficha por id y usuario.

    public Optional<Ficha> obtenerFicha(Long fichaId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<Ficha> ficha = fichaRepository.findById(fichaId);
        return ficha.filter(f -> f.getUsuario().getId().equals(usuario.getId()));
    }

    // Guarda una ficha.

    public void guardarFicha(Ficha ficha, MultipartFile imagenFondo, String email) throws Exception {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ficha.setUsuario(usuario);

        if (!imagenFondo.isEmpty()) {
            Blob imagen = imagenService.saveImage(imagenFondo);
            ficha.setImagen(imagen);
        }

        fichaRepository.save(ficha);
    }

    // Edita una ficha.

    public void editarFicha(Long fichaId, Ficha fichaEditada, String email) {
        Ficha ficha = obtenerFicha(fichaId, email)
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));

        ficha.setNombre(fichaEditada.getNombre());
        ficha.setIdioma(fichaEditada.getIdioma());
        ficha.setAsignatura(fichaEditada.getAsignatura());
        ficha.setContenido(fichaEditada.getContenido());
        ficha.setDescripcion(fichaEditada.getDescripcion());

        fichaRepository.save(ficha);
    }

    // Elimina una ficha.

public void eliminarFicha(Long fichaId, String email) {
    Ficha ficha = obtenerFicha(fichaId, email)
            .orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));

    // Eliminar la ficha de todos los cuadernos y actualizar el contador
    for (Cuaderno cuaderno : ficha.getCuadernos()) {
        cuaderno.getFichas().remove(ficha);
        cuaderno.setNumeroFichas(cuaderno.getNumeroFichas() - 1);
        cuadernoRepository.save(cuaderno);
    }

    ficha.getCuadernos().clear();
    fichaRepository.save(ficha);

    fichaRepository.delete(ficha);
}

    

    // Devuelve la imagen de una ficha.

    public ResponseEntity<Object> obtenerImagenFicha(Long id) {
        Optional<Ficha> ficha = fichaRepository.findById(id);
        if (ficha.isPresent() && ficha.get().getImagen() != null) {
            try {
                return imagenService.getImageResponse(ficha.get().getImagen());
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving image");
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Devuelve las fichas de un usuario.

    public List<Ficha> obtenerFichasPorUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return fichaRepository.findByUsuario(usuario);
    }

    // Devuelve una ficha por id.

    public Optional<Ficha> obtenerFichaPorId(Long fichaId) {
        return fichaRepository.findById(fichaId);
    }

    // Devuelve las fichas de un cuaderno de forma paginada.
    public Page<Ficha> obtenerFichasPaginadas(Long cuadernoId, int page, int size) {

        Page<Ficha> fichas = fichaRepository.obtenerFichasPorCuaderno(cuadernoId, PageRequest.of(page, size));

        return fichas;

    }

    // Devuelve las fichas no agregadas a un cuaderno de forma paginada.

    public Page<Ficha> obtenerFichasPaginadasNoAgregadas(Long cuadernoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichaRepository.findFichasNoAgregadas(cuadernoId, pageable);
    }

    public Page<Ficha> obtenerFichasPaginadasPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return fichaRepository.findByUsuario(usuario, pageable);
    }

    // Guarda los elementos superpuestos en formato JSON
    public void guardarElementosSuperpuestos(Long fichaId, String elementosJson, String email) {
        Ficha ficha = obtenerFicha(fichaId, email)
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));

        ficha.setElementosSuperpuestos(elementosJson);
        fichaRepository.save(ficha);
    }

}
