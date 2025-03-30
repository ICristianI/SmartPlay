package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Ficha;
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

    List<Juego> findByUsuario(User usuario);

    Optional<Juego> findByIdAndUsuario_Email(Long id, String email);

    Page<Juego> findByUsuario(User usuario, Pageable pageable);

    @Query("SELECT j FROM Juego j JOIN j.cuadernos c WHERE c.id = :cuadernoId")
    Page<Juego> obtenerJuegosPorCuaderno(@Param("cuadernoId") Long cuadernoId, Pageable pageable);

    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario AND j NOT IN (SELECT j FROM Juego j JOIN j.cuadernos c WHERE c.id = :cuadernoId)")
    List<Juego> findJuegosNoAgregados(@Param("cuadernoId") Long cuadernoId, @Param("usuario") User usuario);

    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario")
    Page<Juego> obtenerJuegosPaginados(@Param("usuario") User usuario, Pageable pageable);
    
    @Query("SELECT j FROM Juego j WHERE j.privada = false")
    Page<Juego> findAll(Pageable pageable);

    Page<Juego> findByNombreContainingIgnoreCaseAndPrivadaFalse(String nombre, Pageable pageable);

    Page<Juego> findAllByOrderByFechaCreacionDesc(Pageable pageable);
    Page<Juego> findByNombreContainingIgnoreCaseOrderByFechaCreacionDesc(String nombre, Pageable pageable);

    Page<Juego> findAllByOrderByMeGustaDesc(Pageable pageable);
    Page<Juego> findByNombreContainingIgnoreCaseOrderByMeGustaDesc(String nombre, Pageable pageable);

}
