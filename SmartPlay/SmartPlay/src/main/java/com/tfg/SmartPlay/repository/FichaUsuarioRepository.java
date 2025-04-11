package com.tfg.SmartPlay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaUsuario;
import com.tfg.SmartPlay.entity.User;

@Repository
public interface FichaUsuarioRepository extends JpaRepository<FichaUsuario, Long> {
    Optional<FichaUsuario> findByFichaAndUsuario(Ficha ficha, Optional<User> user);
    Optional<FichaUsuario> findByFichaIdAndUsuarioId(Long fichaId, Long usuarioId);
}
