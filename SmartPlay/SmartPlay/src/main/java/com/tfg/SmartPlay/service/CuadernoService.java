package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Servicio para gestionar los cuadernos de un usuario.

@Service
public class CuadernoService {

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private FichaService fichaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JuegoService juegoService;

    // Devuelve todos los cuadernos de un usuario Paginados.

    public Page<Cuaderno> listarCuadernosPaginados(String usuarioEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.findByUsuarioEmail(usuarioEmail, pageable);
    }

    // Devuelve un cuaderno por usuario e id.

    public Optional<Cuaderno> obtenerCuadernoPorIdYUsuario(Long cuadernoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<Cuaderno> cuaderno = cuadernoRepository.findById(cuadernoId);
        return cuaderno.filter(c -> c.getUsuario().getId().equals(usuario.getId()));
    }


    // Devuelve un cuaderno por id.
    public Optional<Cuaderno> obtenerCuadernoPorId(Long id) {
        return cuadernoRepository.findById(id);
    }

    /**
     * Devuelve las fichas NO agregadas a un cuaderno específico.
     */
    public List<Ficha> obtenerFichasNoAgregadas(Long cuadernoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<Cuaderno> cuadernoOpt = cuadernoRepository.findById(cuadernoId);
        if (cuadernoOpt.isEmpty()) {
            return List.of();
        }

        Cuaderno cuaderno = cuadernoOpt.get();
        List<Ficha> todasLasFichas = fichaRepository.findByUsuario(usuario);

        // Filtra las fichas que NO están en el cuaderno
        return todasLasFichas.stream()
                .filter(ficha -> !cuaderno.getFichas().contains(ficha))
                .collect(Collectors.toList());
    }

    /**
     * Guarda un nuevo cuaderno con fichas y juegos seleccionados.
     */
    public Cuaderno guardarCuaderno(Cuaderno cuaderno, List<Long> fichasIds, List<Long> juegosIds) {
        User usuario = userComponent.getUser().get();
        cuaderno.setUsuario(usuario);

        List<Ficha> fichasSeleccionadas = fichaRepository.findAllById(fichasIds);
        List<Juego> juegosSeleccionados = juegoRepository.findAllById(juegosIds);

        cuaderno.setFichas(fichasSeleccionadas);
        cuaderno.setJuegos(juegosSeleccionados);

        cuaderno.setNumeroFichas(fichasSeleccionadas.size());
        cuaderno.setNumeroJuegos(juegosSeleccionados.size());

        return cuadernoRepository.save(cuaderno);
    }

    /**
     * Edita un cuaderno permitiendo agregar fichas y juegos.
     */
    public boolean editarCuaderno(Long cuadernoId, String nuevoNombre, List<Long> nuevasFichasIds,
            List<Long> nuevosJuegosIds, String email) {
        Optional<User> usuarioOpt = userRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        User usuario = usuarioOpt.get();
        Optional<Cuaderno> cuadernoOpt = cuadernoRepository.findById(cuadernoId);

        if (cuadernoOpt.isPresent() && cuadernoOpt.get().getUsuario().getId().equals(usuario.getId())) {
            Cuaderno cuaderno = cuadernoOpt.get();

            if (!nuevoNombre.isBlank()) {
                cuaderno.setNombre(nuevoNombre);
            }

            if (nuevasFichasIds != null && !nuevasFichasIds.isEmpty()) {
                List<Ficha> nuevasFichas = fichaRepository.findAllById(nuevasFichasIds);
                cuaderno.getFichas().addAll(nuevasFichas);
                cuaderno.setNumeroFichas(cuaderno.getNumeroFichas() + nuevasFichas.size());
            }

            if (nuevosJuegosIds != null && !nuevosJuegosIds.isEmpty()) {
                List<Juego> nuevosJuegos = juegoRepository.findAllById(nuevosJuegosIds);
                cuaderno.getJuegos().addAll(nuevosJuegos);
                cuaderno.setNumeroJuegos(cuaderno.getNumeroJuegos() + nuevosJuegos.size());
            }

            cuadernoRepository.save(cuaderno);
            return true;
        }

        return false;
    }

    /**
     * Elimina una ficha del cuaderno.
     */
    public boolean eliminarFichaDeUsuario(Long fichaId, Long cuadernoId, String email) {
        Optional<Cuaderno> cuadernoOpt = obtenerCuadernoPorId(cuadernoId);
        Optional<Ficha> fichaOpt = fichaService.obtenerFichaPorId(fichaId);

        if (cuadernoOpt.isPresent() && fichaOpt.isPresent()) {
            Cuaderno cuaderno = cuadernoOpt.get();
            Ficha ficha = fichaOpt.get();

            boolean eliminado = cuaderno.getFichas().remove(ficha);
            cuaderno.setNumeroFichas(cuaderno.getNumeroFichas() - 1);

            if (eliminado) {
                cuadernoRepository.save(cuaderno);
            }

            return eliminado;
        }

        return false;
    }

    /**
     * Elimina un juego del cuaderno.
     */
    public boolean eliminarJuegoDeUsuario(Long juegoId, Long cuadernoId, String email) {
        Optional<Cuaderno> cuadernoOpt = obtenerCuadernoPorId(cuadernoId);
        Optional<Juego> juegoOpt = juegoRepository.findById(juegoId);

        if (cuadernoOpt.isPresent() && juegoOpt.isPresent()) {
            Cuaderno cuaderno = cuadernoOpt.get();
            Juego juego = juegoOpt.get();

            boolean eliminado = cuaderno.getJuegos().remove(juego);

            if (eliminado) {
                cuaderno.setNumeroJuegos(cuaderno.getNumeroJuegos() - 1);
                cuadernoRepository.save(cuaderno);
            }

            return eliminado;
        }

        return false;
    }

    /**
     * Método para eliminar un cuaderno si el usuario autenticado es el propietario.
     */
    public boolean eliminarCuaderno(Long cuadernoId, String email) {
        Optional<User> usuarioOpt = userRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        User usuario = usuarioOpt.get();
        Optional<Cuaderno> cuadernoOpt = cuadernoRepository.findById(cuadernoId);

        if (cuadernoOpt.isPresent() && cuadernoOpt.get().getUsuario().getId().equals(usuario.getId())) {
            cuadernoRepository.deleteById(cuadernoId);
            return true;
        }

        return false;
    }

    // Devuelve los cuadernos que contienen una ficha específica.

    public List<Cuaderno> obtenerCuadernosConFicha(Ficha ficha) {
        return cuadernoRepository.findByFichasContaining(ficha);
    }

    // Devuelve los juegos que no han sido agregados a un cuaderno.
    public List<Juego> obtenerJuegosNoAgregados(Long cuadernoId, String email) {
        return juegoService.obtenerJuegosNoAgregados(cuadernoId, email);
    }

    // Devuelve los juegos de un cuaderno paginados.
    public Page<Juego> obtenerJuegosPaginados(Long cuadernoId, int page, int size) {
        return juegoService.obtenerJuegosPaginadosEnCuaderno(cuadernoId, page, size);
    }

    // Devuelve los cuadernos que contienen una ficha específica paginados.
    public Page<Cuaderno> obtenerCuadernosConFichaPaginados(Ficha ficha, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.findByFichasContaining(ficha, pageable);
    }

    public List<Cuaderno> listarCuadernosPorUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return cuadernoRepository.findByUsuario(usuario);
    }
    
}
