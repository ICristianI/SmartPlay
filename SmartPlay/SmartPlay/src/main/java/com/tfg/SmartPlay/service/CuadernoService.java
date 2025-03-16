package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CuadernoService {

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private FichaService fichaService;

    @Autowired
    private UserRepository userRepository;

public Page<Cuaderno> listarCuadernosPaginados(String usuarioEmail, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return cuadernoRepository.findByUsuarioEmail(usuarioEmail, pageable);
}


    public Optional<Cuaderno> obtenerCuadernoPorIdYUsuario(Long cuadernoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<Cuaderno> cuaderno = cuadernoRepository.findById(cuadernoId);
        return cuaderno.filter(c -> c.getUsuario().getId().equals(usuario.getId()));
    }
    public List<Cuaderno> obtenerCuadernosPorUsuario(User usuario) {
        return cuadernoRepository.findByUsuario(usuario);
    }

    public Optional<Cuaderno> obtenerCuadernoPorId(Long id) {
        return cuadernoRepository.findById(id);
    }

    public Cuaderno guardarCuaderno(Cuaderno cuaderno, List<Long> fichasIds) {
        User usuario = userComponent.getUser().get();
        cuaderno.setUsuario(usuario);
    
        List<Ficha> fichasSeleccionadas = fichaRepository.findAllById(fichasIds);
        cuaderno.setFichas(fichasSeleccionadas);

        cuaderno.setNumeroFichas(fichasSeleccionadas.size());
    
        return cuadernoRepository.save(cuaderno);
    }  

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

    
    public boolean editarCuaderno(Long cuadernoId, String nuevoNombre, List<Long> nuevasFichasIds, String email) {
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
    
            cuadernoRepository.save(cuaderno);
            return true;
        }
    
        return false;
    }
    

    public boolean eliminarFichaDeUsuario(Long fichaId, Long cuadernoId,String email) {
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


    public List<Cuaderno> obtenerCuadernosConFicha(Ficha ficha) {
        return cuadernoRepository.findByFichasContaining(ficha);
    }

    public Page<Cuaderno> obtenerCuadernosConFichaPaginados(Ficha ficha, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.findByFichasContaining(ficha, pageable);
    }
    
    
}