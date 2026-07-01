package com.desi.jeques.entity;

import java.time.LocalDateTime;

import com.desi.jeques.utilidades.EstadoIncidente;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_estado_incidente")
public class HistorialEstadoIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoIncidente estado;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", nullable = false)
    private Incidente incidente;

    public HistorialEstadoIncidente() {
    }
}