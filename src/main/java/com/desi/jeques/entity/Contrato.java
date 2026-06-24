package com.desi.jeques.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "contratos")
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
 public String getEstado() {
		return estado;
	   }

public void setEstado(String estado) {
		this.estado = estado;
	   }


public Boolean getEliminado() {
	return eliminado;
}

public void setEliminado(Boolean eliminado) {
	this.eliminado = eliminado;
}
		   
		   
	@ManyToOne(optional = false)
    @JoinColumn(name = "inquilino_id")
    private Persona inquilino;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Column(nullable = false)
    @Min(value = 1, message = "La duración debe ser un número positivo")
    private Integer duracionMeses;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = "El importe debe ser positivo")
    private BigDecimal importeMensual;
    
    @Column(nullable = false)
    @Min(value = 1, message = "El día de vencimiento debe ser mayor o igual a 1")
    @Max(value = 31, message = "El día de vencimiento debe ser menor o igual a 31")
    private Integer diaVencimientoMensual;
    
    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(nullable = false, length = 150)
    private String estado= "Activo";
    
    @Column(nullable = false, length = 150)
    private Boolean eliminado= false;    
    
   

}
