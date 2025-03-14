package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private UserComponent userComponent;

    public List<Ficha> listarFichas(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return fichaRepository.findByUsuario(usuario);
    }

    public Optional<Ficha> obtenerFicha(Long fichaId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<Ficha> ficha = fichaRepository.findById(fichaId);
        return ficha.filter(f -> f.getUsuario().getId().equals(usuario.getId()));
    }

    public void editarFicha(Long fichaId, Ficha fichaEditada, String email) {
        Ficha ficha = obtenerFicha(fichaId, email).orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));
        ficha.setNombre(fichaEditada.getNombre());
        ficha.setIdioma(fichaEditada.getIdioma());
        ficha.setAsignatura(fichaEditada.getAsignatura());
        ficha.setContenido(fichaEditada.getContenido());
        ficha.setDescripcion(fichaEditada.getDescripcion());
        fichaRepository.save(ficha);
    }

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

    public void guardarFicha(Ficha ficha, MultipartFile imagenFondo) throws Exception {
        if (!imagenFondo.isEmpty()) {
            Blob imagen = imagenService.saveImage(imagenFondo);
            ficha.setImagen(imagen);
        }
        ficha.setUsuario(userComponent.getUser().orElse(null));
        fichaRepository.save(ficha);
    }

    public void eliminarFicha(Long fichaId, String email) {
        Ficha ficha = obtenerFicha(fichaId, email).orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));
        fichaRepository.deleteById(fichaId);
    }

    public List<Ficha> obtenerFichasPorUsuario(User usuario) {
        return fichaRepository.findByUsuario(usuario);
    }

    public List<Ficha> obtenerFichasPorIds(List<Long> ids) {
        return fichaRepository.findAllById(ids);
    }

    public Optional<Ficha> obtenerFichaPorId(Long fichaId) {
        return fichaRepository.findById(fichaId);
    }
    
}
