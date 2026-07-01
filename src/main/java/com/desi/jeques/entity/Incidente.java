package com.desi.jeques.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.desi.jeques.utilidades.CategoriaIncidente;
import com.desi.jeques.utilidades.EstadoIncidente;
import com.desi.jeques.utilidades.PrioridadIncidente;

import jakarta.persistence.*;

@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaIncidente categoria;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadIncidente prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoIncidente estado;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "observaciones_resolucion", length = 1000)
    private String observacionesResolucion;

    @Column(name = "costo_resolucion", precision = 12, scale = 2)
    private BigDecimal costoResolucion;

    @Column(name = "responsable", length = 150)
    private String responsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;

    public Incidente() {
    }
}