package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "cuadernos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuaderno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String nombre;

    @Column(nullable = false, unique = false)
    private Integer numeroFichas;

    @Column(nullable = false, unique = false)
    private Integer numeroJuegos;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private Blob imagen;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToMany
    @JoinTable(name = "cuaderno_fichas", joinColumns = @JoinColumn(name = "cuaderno_id"), inverseJoinColumns = @JoinColumn(name = "ficha_id"))
    private List<Ficha> fichas;

    @ManyToMany
    @JoinTable(name = "cuaderno_juegos", joinColumns = @JoinColumn(name = "cuaderno_id"), inverseJoinColumns = @JoinColumn(name = "juego_id"))
    private List<Juego> juegos;

    @ManyToMany(mappedBy = "cuadernos")
    private List<Grupo> grupos;

    
    
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fechaCreacion.format(formatter);
    }


}
