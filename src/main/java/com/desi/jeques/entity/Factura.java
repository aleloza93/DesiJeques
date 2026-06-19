package com.desi.jeques.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

enum EstadoFactura {
    PENDIENTE,
    PAGADA,
    VENCIDA,
    ANULADA
}

enum MedioPago {
    TRANSFERENCIA,
    EFECTIVO,
    DEBITO,
    CREDITO
}

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private boolean eliminada;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medio;

    private BigDecimal importePagado;
    private BigDecimal interes;
    private String conceptoFacturado;
    
    
    //Metodos
    public long getId() {
    	return id;
    }
    
    public LocalDate getFechaEmision() {
    	return fechaEmision;
    }
    
	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public MedioPago getMedio() {
		return medio;
	}

	public void setMedio(MedioPago medio) {
		this.medio = medio;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(BigDecimal importePagado) {
		this.importePagado = importePagado;
	}

	public BigDecimal getInteres() {
		return interes;
	}

	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}

	public String getConceptoFacturado() {
		return conceptoFacturado;
	}

	public void setConceptoFacturado(String conceptoFacturado) {
		this.conceptoFacturado = conceptoFacturado;
	}

	public List<HistorialEstadoFactura> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) {
		this.historialEstados = historialEstados;
	}


	/*
    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;
*/   
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<HistorialEstadoFactura> historialEstados;


}
