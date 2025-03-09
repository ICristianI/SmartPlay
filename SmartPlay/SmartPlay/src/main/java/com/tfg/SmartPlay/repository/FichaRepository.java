package com.tfg.SmartPlay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;

@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {

    List<Ficha> findByNombreContainingIgnoreCase(String nombre);

    List<Ficha> findByAsignaturaContainingIgnoreCase(String asignatura);

    List<Ficha> findByIdiomaContainingIgnoreCase(String idioma);

    Optional<Ficha> findById(Long id);

    List<Ficha> findByUsuario(User usuario);
}
