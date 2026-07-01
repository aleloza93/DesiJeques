package com.desi.jeques.entity;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    @Column(name = "precio_mensual")
    private BigDecimal precioMensual;

    @Column(name = "condiciones_alquiler", columnDefinition = "TEXT")
    private String condicionesAlquiler;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    // activa, pausada, finalizada
    private String estado;

    private boolean eliminada = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getCondicionesAlquiler() {
        return condicionesAlquiler;
    }

    public void setCondicionesAlquiler(String condicionesAlquiler) {
        this.condicionesAlquiler = condicionesAlquiler;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }
}
