package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRoles(String role);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNombre(String nombre);

    Optional<User> findByNombre(String nombre);
}
