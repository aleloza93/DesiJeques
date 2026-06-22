package com.desi.jeques.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.desi.jeques.utilidades.*;
import jakarta.persistence.*;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate fechaEmision;
    
    @Column(nullable = false)
    private LocalDate fechaVencimiento;
    
    @Column(nullable = false)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;
    
    @Column(nullable = false)
    private boolean eliminada;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medio;

    private BigDecimal importePagado;
    private BigDecimal interes;
    
    @Column(nullable = false)
    private String conceptoFacturado;
    
    
    //GETTERS
    public Long getId() {return id;}    
    public LocalDate getFechaEmision() {return fechaEmision;}
    public LocalDate getFechaVencimiento() {return fechaVencimiento;}
    public BigDecimal getImporte() {return importe;}
    public EstadoFactura getEstado() {return estado;}
    public boolean isEliminada() {return eliminada;}
    public LocalDate getFechaPago() {return fechaPago;}
    public MedioPago getMedio() {return medio;}
    public BigDecimal getImportePagado() {return importePagado;}
    public BigDecimal getInteres() {return interes;}
    public String getConceptoFacturado() {return conceptoFacturado;}
    
    public Contrato getContrato() {
        return contrato;
    	}
    
    public List<HistorialEstadoFactura> getHistorialEstados() {return historialEstados;}
    
    //SETTERS
    public void setFechaEmision(LocalDate fechaEmision) {this.fechaEmision = fechaEmision;}
	public void setFechaVencimiento(LocalDate fechaVencimiento) {this.fechaVencimiento = fechaVencimiento;}
	public void setImporte(BigDecimal importe) {this.importe = importe;}
	public void setEstado(EstadoFactura estado) {this.estado = estado;}
	public void setEliminada(boolean eliminada) {this.eliminada = eliminada;}	
	public void setFechaPago(LocalDate fechaPago) {this.fechaPago = fechaPago;}
	public void setMedio(MedioPago medio) {this.medio = medio;}
	public void setImportePagado(BigDecimal importePagado) {this.importePagado = importePagado;}
	public void setInteres(BigDecimal interes) {this.interes = interes;}
	public void setConceptoFacturado(String conceptoFacturado) {this.conceptoFacturado = conceptoFacturado;}
	
	public void setContrato(Contrato contrato) {
	    this.contrato = contrato;
		}

	public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) {
		this.historialEstados = historialEstados;
	}
		
	//Relaciones
	
    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato; 
  
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<HistorialEstadoFactura> historialEstados;


}
