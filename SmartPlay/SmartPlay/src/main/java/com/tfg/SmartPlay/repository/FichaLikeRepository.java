package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaLike;
import com.tfg.SmartPlay.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FichaLikeRepository extends JpaRepository<FichaLike, Long> {
    boolean existsByFichaAndUsuario(Ficha ficha, User usuario);
    Optional<FichaLike> findByFichaAndUsuario(Ficha ficha, User usuario);
    int countByFicha(Ficha ficha);
    void deleteByFicha_Id(Long fichaId);
}
