package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    /**
     * Obtiene los juegos creados por un usuario.
     */
    List<Juego> findByUsuario(User usuario);

    /**
     * Obtiene un juego por su ID y el usuario propietario.
     */
    Optional<Juego> findByIdAndUsuario_Email(Long id, String email);

    /**
     * Obtiene los juegos de un usuario paginados.
     */
    Page<Juego> findByUsuario(User usuario, Pageable pageable);

    /**
     * Obtiene los juegos que pertenecen a un cuaderno específico.
     */
    @Query("SELECT j FROM Juego j JOIN j.cuadernos c WHERE c.id = :cuadernoId")
    Page<Juego> obtenerJuegosPorCuaderno(@Param("cuadernoId") Long cuadernoId, Pageable pageable);

    /**
     * Obtiene los juegos que NO están en un cuaderno.
     */
    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario AND j NOT IN (SELECT j FROM Juego j JOIN j.cuadernos c WHERE c.id = :cuadernoId)")
    List<Juego> findJuegosNoAgregados(@Param("cuadernoId") Long cuadernoId, @Param("usuario") User usuario);

    /**
     * Obtiene los juegos de un usuario paginados.
     */
    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario")
    Page<Juego> obtenerJuegosPaginados(@Param("usuario") User usuario, Pageable pageable);

}
