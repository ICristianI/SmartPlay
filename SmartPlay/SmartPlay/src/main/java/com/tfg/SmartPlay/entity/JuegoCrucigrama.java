package com.tfg.SmartPlay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JuegoCrucigrama extends Juego {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pistas;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String respuestas;

}
