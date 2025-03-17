package com.tfg.SmartPlay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuegoSopaLetras extends Juego {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String gridJson; // Guarda la matriz en JSON

    @Column(columnDefinition = "TEXT", nullable = false)
    private String palabrasJson; // Guarda las palabras en JSON
}

