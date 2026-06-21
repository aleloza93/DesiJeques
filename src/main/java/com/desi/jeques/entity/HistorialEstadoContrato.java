package com.desi.jeques.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_contrato")
public class HistorialEstadoContrato {
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(LocalDateTime fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;
	
	
}