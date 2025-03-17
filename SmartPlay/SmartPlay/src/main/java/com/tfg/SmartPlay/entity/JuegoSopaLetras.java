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
public class JuegoSopaLetras extends Juego {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String gridJson;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String palabrasJson;
}
