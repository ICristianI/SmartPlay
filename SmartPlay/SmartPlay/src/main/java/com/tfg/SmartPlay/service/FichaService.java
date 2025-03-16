package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
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

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService;

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
        Ficha ficha = obtenerFicha(fichaId, email)
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));

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

    public void eliminarFicha(Long fichaId, String email) {
        Ficha ficha = obtenerFicha(fichaId, email)
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada o sin permisos"));

        fichaRepository.deleteById(fichaId);
    }

    public List<Ficha> obtenerFichasPorUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return fichaRepository.findByUsuario(usuario);
    }

    public Optional<Ficha> obtenerFichaPorId(Long fichaId) {
        return fichaRepository.findById(fichaId);
    }

    public Page<Ficha> obtenerFichasPaginadas(Long cuadernoId, int page, int size) {

        Page<Ficha> fichas = fichaRepository.obtenerFichasPorCuaderno(cuadernoId, PageRequest.of(page, size));

        System.out.println("Fichas recuperadas para el cuaderno " + cuadernoId + ": " + fichas.getContent().size());

        return fichas;

    }

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

    
    

}
