package com.desi.jeques.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "provincias")
public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    public Provincia() {
    }
}