package com.desi.jeques.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.desi.jeques.entity.Factura;
import com.desi.jeques.utilidades.EstadoFactura;
import com.desi.jeques.utilidades.MedioPago;


public interface FacturaService {
  
	Factura crearFactura(Long contratoId,
			String conceptoFacturado,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            BigDecimal importe,
            LocalDate fechaPago,
            MedioPago medioPago,
            BigDecimal importePagado,
            BigDecimal interesPagado);
	
	List<Factura> listarModificables();

    Factura obtenerPorId(Long id);

    Factura modificarFactura(Long id,
    		EstadoFactura nuevoEstado,
            String conceptoFacturado,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            BigDecimal importe,
            LocalDate fechaPago,
            MedioPago medioPago,
            BigDecimal importePagado,
            BigDecimal interes);
    
    boolean validarTransicionEstado(EstadoFactura estadoActual, EstadoFactura nuevoEstado);


}
