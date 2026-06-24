package com.desi.jeques.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.Factura;
import com.desi.jeques.entity.HistorialEstadoFactura;
import com.desi.jeques.repository.FacturaRepository;
import com.desi.jeques.repository.HistorialEstadoFacturaRepository;
import com.desi.jeques.service.ContratoService;
import com.desi.jeques.service.FacturaService;
import com.desi.jeques.utilidades.EstadoFactura;
import com.desi.jeques.utilidades.MedioPago;

import jakarta.transaction.Transactional;

@Service
public class FacturaServiceImpl implements FacturaService {
	
	@Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private HistorialEstadoFacturaRepository historialRepository;
    
    @Override
    @Transactional
    public Factura crearFactura(Long contratoId, String conceptoFacturado,
            LocalDate fechaEmision, LocalDate fechaVencimiento, BigDecimal importe,
            LocalDate fechaPago, MedioPago medioPago, BigDecimal importePagado, BigDecimal interesPagado) {
    	
    	//Verifica fecha de vencimiento sobre la fecha de emision si es valido
        if (fechaVencimiento.isBefore(fechaEmision)) {
            throw new IllegalArgumentException(
                "La fecha de vencimiento debe ser igual o posterior a la de emisión");
        }
        
        //Verifica que el importe no sea >=0
        if (importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo");
        }
        
        if (importePagado != null && importePagado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El importe pagado no puede ser negativo");
        }
        
        if (interesPagado != null && interesPagado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El interés pagado no puede ser negativo");
        }
        
        Contrato contrato = contratoService.buscarPorId(contratoId); 

        if (!contratoService.puedeFacturarse(contrato)) {
            throw new IllegalStateException(
                "No se puede crear una factura para este contrato en su estado actual");
        }

        Factura factura = new Factura();
        factura.setContrato(contrato);
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaVencimiento);
        factura.setImporte(contrato.getImporteMensual());
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setEliminada(false);
        factura.setConceptoFacturado(conceptoFacturado);
        factura.setFechaPago(fechaPago);
        factura.setMedio(medioPago);
        factura.setImportePagado(importePagado);
        factura.setInteres(interesPagado);
   
        Factura facturaGuardada = facturaRepository.save(factura);
        
        
        
        //y si muere la base de datos antes de esto ???	
        HistorialEstadoFactura historial = new HistorialEstadoFactura();
        historial.setFactura(facturaGuardada);
        historial.setEstado(EstadoFactura.PENDIENTE);
        historial.setFechaHora(LocalDateTime.now());
        historialRepository.save(historial);

        return facturaGuardada;
    }
   
}
