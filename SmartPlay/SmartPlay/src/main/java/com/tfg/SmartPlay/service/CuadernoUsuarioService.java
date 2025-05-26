package com.tfg.SmartPlay.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.CuadernoUsuario;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.CuadernoUsuarioRepository;
import com.tfg.SmartPlay.repository.FichaUsuarioRepository;

@Service
public class CuadernoUsuarioService {

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private CuadernoUsuarioRepository cuadernoUsuarioRepository;

    @Autowired
    private FichaUsuarioRepository fichaUsuarioRepository;

    public void actualizarNota(Long cuadernoId, Optional<User> userOpt) {
        if (userOpt.isEmpty()) return;
        User user = userOpt.get();

        Cuaderno cuaderno = cuadernoRepository.findById(cuadernoId)
                .orElseThrow(() -> new RuntimeException("Cuaderno no encontrado"));

        List<Ficha> fichas = cuaderno.getFichas();
        if (fichas == null || fichas.isEmpty()) return;

        // Obtener las notas del usuario para las fichas del cuaderno
        List<Double> notas = fichas.stream()
                .map(f -> fichaUsuarioRepository.findByFichaAndUsuario(f, userOpt))
                .filter(Optional::isPresent)
                .map(opt -> opt.get().getNota())
                .collect(Collectors.toList());

        if (notas.isEmpty()) return;

        double media = notas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double notaRedondeada = Math.round(media * 10.0) / 10.0;

        CuadernoUsuario cuadernoUsuario = cuadernoUsuarioRepository.findByCuadernoAndUsuario(cuaderno, userOpt)
                .orElseGet(() -> {
                    CuadernoUsuario nuevo = new CuadernoUsuario();
                    nuevo.setCuaderno(cuaderno);
                    nuevo.setUsuario(user);
                    nuevo.setIntentos(0);
                    return nuevo;
                });

        cuadernoUsuario.setNota(notaRedondeada);
        cuadernoUsuario.setIntentos(cuadernoUsuario.getIntentos() + 1);

        cuadernoUsuarioRepository.save(cuadernoUsuario);
    }

    public Optional<CuadernoUsuario> obtenerNota(Long cuadernoId, Long userId) {
        return cuadernoUsuarioRepository.findByCuadernoIdAndUsuarioId(cuadernoId, userId);
    }
}
