package com.tfg.SmartPlay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;

@Repository
public interface CuadernoRepository extends JpaRepository<Cuaderno, Long> {

    List<Cuaderno> findByUsuario(User usuario);

    List<Cuaderno> findByFichasContaining(Ficha ficha);
}
