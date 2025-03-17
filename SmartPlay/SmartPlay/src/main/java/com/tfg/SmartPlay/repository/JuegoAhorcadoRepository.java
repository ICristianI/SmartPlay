package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JuegoAhorcadoRepository extends JpaRepository<JuegoAhorcado, Long> {

    // Obtener todos los juegos de un usuario
    List<JuegoAhorcado> findByUsuario(User usuario);

    // Obtener juegos paginados por usuario
    Page<JuegoAhorcado> findByUsuario(User usuario, Pageable pageable);
}
