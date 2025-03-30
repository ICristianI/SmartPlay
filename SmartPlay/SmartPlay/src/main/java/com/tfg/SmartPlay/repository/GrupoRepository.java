package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Grupo;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /**
     * Devuelve los grupos en los que participa un usuario (paginados).
     */
    @Query("SELECT g FROM Grupo g JOIN g.usuarios u WHERE u = :usuario")
    Page<Grupo> findByUsuariosContaining(@Param("usuario") User usuario, Pageable pageable);

    /**
     * Busca un grupo por su código de acceso único.
     */
    Optional<Grupo> findByCodigoAcceso(String codigoAcceso);

    /**
     * Comprueba si ya existe un grupo con ese código (para evitar duplicados).
     */
    boolean existsByCodigoAcceso(String codigoAcceso);

    @Query("SELECT g FROM Grupo g JOIN g.cuadernos c WHERE g.id = :grupoId")
    Page<Cuaderno> obtenerCuadernosPaginados(@Param("grupoId") Long grupoId, Pageable pageable);


}
