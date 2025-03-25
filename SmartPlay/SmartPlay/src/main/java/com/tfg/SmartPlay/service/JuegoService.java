package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private UserRepository userRepository;


    /**
 * Obtiene todos los juegos de un usuario sin paginación.
 */
    public List<Juego> obtenerTodosLosJuegosPorUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return juegoRepository.findByUsuario(usuario);
    }

    /**
     * Obtiene todos los juegos de un usuario paginados.
     */
    public Page<Juego> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Pageable pageable = PageRequest.of(page, size);
        return juegoRepository.obtenerJuegosPaginados(usuario, pageable);
    }

    /**
     * Obtiene un juego por ID y verifica si el usuario es el propietario.
     */
    public Optional<Juego> obtenerJuego(Long juegoId, String email) {
        return juegoRepository.findByIdAndUsuario_Email(juegoId, email);
    }

    /**
     * Guarda un nuevo juego para un usuario.
     */
    public void guardarJuego(Juego juego, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        juego.setUsuario(usuario);
        juegoRepository.save(juego);
    }

    /**
     * Edita un juego si pertenece al usuario autenticado.
     */
    public void editarJuego(Long juegoId, Juego juegoEditado, String email) {
        Juego juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setComentarios(juegoEditado.getComentarios());

        juegoRepository.save(juego);
    }

    /**
     * Elimina un juego si pertenece al usuario autenticado.
     */

    public void eliminarJuego(Long juegoId, String email) {
        Juego juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));
    
        for (var cuaderno : juego.getCuadernos()) {
            cuaderno.getJuegos().remove(juego);
            cuaderno.setNumeroJuegos(Math.max(0, cuaderno.getNumeroJuegos() - 1));
        }
    
        juego.getCuadernos().clear();
        juegoRepository.save(juego);
    
        juegoRepository.delete(juego);
    }
    
    /**
     * Obtiene juegos no agregados a un cuaderno.
     */
    public List<Juego> obtenerJuegosNoAgregados(Long cuadernoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return juegoRepository.findJuegosNoAgregados(cuadernoId, usuario);
    }

    /**
     * Obtiene juegos pertenecientes a un cuaderno de forma paginada.
     */
    public Page<Juego> obtenerJuegosPaginadosEnCuaderno(Long cuadernoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return juegoRepository.obtenerJuegosPorCuaderno(cuadernoId, pageable);
    }


    public Optional<Juego> obtenerJuegoPorId(Long id) {
        return juegoRepository.findById(id);
    }
}
