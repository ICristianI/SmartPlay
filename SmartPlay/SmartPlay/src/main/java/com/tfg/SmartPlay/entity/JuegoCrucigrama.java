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
public class JuegoCrucigrama extends Juego {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pistasJson; // Guarda la lista de pistas en JSON

    @Column(columnDefinition = "TEXT", nullable = false)
    private String respuestasJson; // Guarda la lista de respuestas en JSON

    @Column(columnDefinition = "TEXT", nullable = false)
    private String gridJson; // Representación del crucigrama en JSON
}
