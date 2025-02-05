package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fichas_interactivas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaInteractiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String nombre;

    @Column(nullable = false, unique = false)
    private String descripcion;

    @Column(nullable = false, unique = false)
    private String imagenFondo; // Ruta o URL de la imagen.

    @Column(columnDefinition = "TEXT")
    private String elementosSuperpuestos; // JSON o texto que describa los elementos.

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

}
