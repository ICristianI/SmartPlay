package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JuegoSopaLetrasRepository extends JpaRepository<JuegoSopaLetras, Long> {

    // Buscar juegos por usuario
    List<JuegoSopaLetras> findByUsuario(User usuario);

    // Buscar juegos por usuario con paginación
    Page<JuegoSopaLetras> findByUsuario(User usuario, Pageable pageable);
}
