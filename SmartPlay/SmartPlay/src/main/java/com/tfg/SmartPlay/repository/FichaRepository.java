package com.tfg.SmartPlay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;

@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {

    List<Ficha> findByNombreContainingIgnoreCase(String nombre);

    List<Ficha> findByAsignaturaContainingIgnoreCase(String asignatura);

    List<Ficha> findByIdiomaContainingIgnoreCase(String idioma);

    Optional<Ficha> findById(Long id);

    List<Ficha> findByUsuario(User usuario);

    @Query("SELECT f FROM Ficha f JOIN f.cuadernos c WHERE c.id = :cuadernoId")
    Page<Ficha> obtenerFichasPorCuaderno(@Param("cuadernoId") Long cuadernoId, Pageable pageable);

    @Query("""
                SELECT f FROM Ficha f
                WHERE f.id NOT IN (
                    SELECT cf.id FROM Cuaderno c
                    JOIN c.fichas cf
                    WHERE c.id = :cuadernoId
                )
            """)
    Page<Ficha> findFichasNoAgregadas(Long cuadernoId, Pageable pageable);

    @Query("SELECT f FROM Ficha f WHERE f.usuario = :usuario")
    Page<Ficha> findByUsuario(@Param("usuario") User usuario, Pageable pageable);

    
    @Query("SELECT f FROM Ficha f WHERE f.privada = false")
    Page<Ficha> findAll(Pageable pageable);
    

}
