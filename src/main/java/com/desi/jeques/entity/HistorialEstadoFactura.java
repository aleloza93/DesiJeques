package com.desi.jeques.entity;

import java.time.LocalDateTime;

import com.desi.jeques.utilidades.EstadoFactura;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_estado_factura") // Para el nombre en la Base de Datos
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private LocalDateTime fechaHora;
    
    
    //GETTERS    
    public Long getId() {return id;}
    public EstadoFactura getEstado() {return estado;}
    public LocalDateTime getFechaHora() {return fechaHora;}
    public Factura getFactura() {return factura;}
    
    //SETTERS
    public void setEstado(EstadoFactura estado) {this.estado = estado;}
    public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora = fechaHora;}
    public void setFactura(Factura factura) {this.factura = factura;}
   
    
    
    //Relaciones
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;


}
