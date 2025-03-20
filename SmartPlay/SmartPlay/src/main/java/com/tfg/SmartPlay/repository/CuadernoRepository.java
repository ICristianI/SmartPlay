package com.tfg.SmartPlay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CuadernoRepository extends JpaRepository<Cuaderno, Long> {

    @Query("SELECT c FROM Cuaderno c WHERE c.usuario.email = :email")
    Page<Cuaderno> findByUsuarioEmail(@Param("email") String email, Pageable pageable);

    List<Cuaderno> findByUsuario(User usuario);

    List<Cuaderno> findByFichasContaining(Ficha ficha);

    @Query("SELECT c FROM Cuaderno c JOIN c.fichas f WHERE f = :ficha")
    Page<Cuaderno> findByFichasContaining(@Param("ficha") Ficha ficha, Pageable pageable);

    @Query("SELECT c FROM Cuaderno c JOIN c.juegos j WHERE j = :juego")
    Page<Cuaderno> obtenerCuadernosPorJuego(@Param("juego") Juego juego, Pageable pageable);

}
