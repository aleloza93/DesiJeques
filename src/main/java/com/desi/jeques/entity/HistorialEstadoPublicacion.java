package com.desi.jeques.entity;

import java.time.LocalDateTime;

import com.desi.jeques.utilidades.EstadoPublicacion;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_estado_publicacion")
public class HistorialEstadoPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPublicacion estado;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    public HistorialEstadoPublicacion() {
    }
}