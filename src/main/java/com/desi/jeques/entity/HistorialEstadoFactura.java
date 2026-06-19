package com.desi.jeques.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "historial_estado_factura")
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
		return id;
	}


	public EstadoFactura getEstado() {
		return estado;
	}


	public LocalDateTime getFechaHora() {
		return fechaHora;
	}


	public Factura getFactura() {
		return factura;
	}


	@Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private LocalDateTime fechaHora;

    
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

}
