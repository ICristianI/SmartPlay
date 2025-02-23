package com.tfg.SmartPlay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.SmartPlay.entity.Ficha;

@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {

    // Buscar fichas por nombre
    List<Ficha> findByNombreContainingIgnoreCase(String nombre);

    // Buscar fichas por asignatura
    List<Ficha> findByAsignaturaContainingIgnoreCase(String asignatura);

    // Buscar fichas por idioma
    List<Ficha> findByIdiomaContainingIgnoreCase(String idioma);

    // Buscar una ficha específica por ID
    Optional<Ficha> findById(Long id);
}
