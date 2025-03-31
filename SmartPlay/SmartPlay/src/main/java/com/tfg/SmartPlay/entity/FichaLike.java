package com.tfg.SmartPlay.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fichas_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "ficha_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "ficha_id", nullable = false)
    private Ficha ficha;
}
