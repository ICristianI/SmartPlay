package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "juegos")
@Inheritance(strategy = InheritanceType.JOINED) // Cada subclase tendrá su propia tabla
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El idioma no puede estar vacío")
    @Size(max = 50, message = "El idioma no puede exceder los 50 caracteres")
    private String idioma;

    @Column(nullable = false)
    @NotBlank(message = "La asignatura no puede estar vacía")
    @Size(max = 50, message = "La asignatura no puede exceder los 50 caracteres")
    private String asignatura;

    @Column(nullable = false)
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 1000, message = "El contenido no puede exceder los 1000 caracteres")
    private String contenido;

    @Column(nullable = false)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToMany
    @JoinTable(
        name = "cuaderno_juegos",
        joinColumns = @JoinColumn(name = "juego_id"),
        inverseJoinColumns = @JoinColumn(name = "cuaderno_id")
    )
    private List<Cuaderno> cuadernos;
}
