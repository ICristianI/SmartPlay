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
public class JuegoCrucigrama extends Juego {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pistas;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String respuestas;

}
