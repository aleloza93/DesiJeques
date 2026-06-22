package com.desi.jeques.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaForm {

	    private Long contratoId;
	    private String conceptoFacturado;
	    private LocalDate fechaEmision;
	    private LocalDate fechaVencimiento;
	    private BigDecimal importe;

	    public Long getContratoId() {
	        return contratoId;
	    }

	    public void setContratoId(Long contratoId) {
	        this.contratoId = contratoId;
	    }

	    public String getConceptoFacturado() {
	        return conceptoFacturado;
	    }

	    public void setConceptoFacturado(String conceptoFacturado) {
	        this.conceptoFacturado = conceptoFacturado;
	    }

	    public LocalDate getFechaEmision() {
	        return fechaEmision;
	    }

	    public void setFechaEmision(LocalDate fechaEmision) {
	        this.fechaEmision = fechaEmision;
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
}
