package com.tfg.SmartPlay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.CuadernoUsuario;
import com.tfg.SmartPlay.entity.User;

public interface CuadernoUsuarioRepository extends JpaRepository<CuadernoUsuario, Long> {

    Optional<CuadernoUsuario> findByCuadernoAndUsuario(Cuaderno cuaderno, Optional<User> usuario);

    Optional<CuadernoUsuario> findByCuadernoIdAndUsuarioId(Long cuadernoId, Long usuarioId);

}
