package com.tfg.SmartPlay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoAhorcado extends Juego {

    @Column(nullable = false)
    private String palabra;

    @Column(nullable = false)
    private int maxIntentos;
}
