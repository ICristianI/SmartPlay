package com.tfg.SmartPlay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tfg.SmartPlay.entity.Cuaderno;

@Repository
public interface CuadernoRepository extends JpaRepository<Cuaderno, Long> {
}
