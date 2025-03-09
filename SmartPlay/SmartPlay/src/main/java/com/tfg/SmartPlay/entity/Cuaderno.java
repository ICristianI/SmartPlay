package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToMany
    @JoinTable(
        name = "cuaderno_fichas",
        joinColumns = @JoinColumn(name = "cuaderno_id"),
        inverseJoinColumns = @JoinColumn(name = "ficha_id")
    )
    private List<Ficha> fichas;
}

