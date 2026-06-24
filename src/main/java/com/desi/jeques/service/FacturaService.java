package com.desi.jeques.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.desi.jeques.entity.Factura;
import com.desi.jeques.utilidades.MedioPago;


public interface FacturaService {
  
	Factura crearFactura(Long contratoId, String conceptoFacturado,
            LocalDate fechaEmision, LocalDate fechaVencimiento, BigDecimal importe,
            LocalDate fechaPago, MedioPago medioPago, BigDecimal importePagado, BigDecimal interesPagado);

}
