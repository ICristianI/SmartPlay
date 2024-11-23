package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRol(User.Rol rol);
    User findByEmail(String email);
}

