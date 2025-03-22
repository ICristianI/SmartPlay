package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuegoCrucigramaRepository extends JpaRepository<JuegoCrucigrama, Long> {
    Page<JuegoCrucigrama> findByUsuario(User usuario, Pageable pageable);
}
