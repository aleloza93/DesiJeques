package com.desi.jeques.entity;

import com.desi.jeques.utilidades.EstadoContrato;

import jakarta.persistence.*;



//Clase creada porque necesitaba para Factura
@Entity
public class Contrato2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContrato estado;

    @Column(nullable = false)
    private boolean eliminado;

    public Long getId() {
        return id;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

}