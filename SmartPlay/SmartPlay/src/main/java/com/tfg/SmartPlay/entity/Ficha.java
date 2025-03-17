package com.tfg.SmartPlay.entity;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fichas_interactivas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ficha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "El idioma no puede estar vacío")
    @Size(max = 50, message = "El idioma no puede exceder los 50 caracteres")
    private String idioma;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "La asignatura no puede estar vacía")
    @Size(max = 50, message = "La asignatura no puede exceder los 50 caracteres")
    private String asignatura;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 1000, message = "El contenido no puede exceder los 1000 caracteres")
    private String contenido;

    @Column(nullable = false, unique = false)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private Blob imagen;

    @Column(columnDefinition = "TEXT")
    private String elementosSuperpuestos;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToMany(mappedBy = "fichas")
    private List<Cuaderno> cuadernos;

}
