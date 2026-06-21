package com.desi.jeques.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.desi.jeques.entity.Contrato2;
import com.desi.jeques.entity.Factura;
import com.desi.jeques.entity.HistorialEstadoFactura;
import com.desi.jeques.repository.FacturaRepository;
import com.desi.jeques.repository.HistorialEstadoFacturaRepository;
import com.desi.jeques.service.FacturaService;
import com.desi.jeques.utilidades.EstadoFactura;

@Service
public class FacturaServiceImpl implements FacturaService {
	
	@Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private Contrato2Service contratoService; // Aca deberia cambiar el Contrato2Service 

    @Autowired
    private HistorialEstadoFacturaRepository historialRepository;
    
    @Override
    public Factura crearFactura(Long contratoId, String conceptoFacturado,
    		LocalDate fechaEmision, LocalDate fechaVencimiento, BigDecimal importe) {

        if (fechaVencimiento.isBefore(fechaEmision)) {
            throw new IllegalArgumentException(
                "La fecha de vencimiento debe ser igual o posterior a la de emisión");
        }

        if (importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo");
        }
        
        Contrato2 contrato = contratoService.obtenerPorId(contratoId); // Aca deberia cambiar el Contrato2 

        if (!contratoService.puedeFacturarse(contrato)) {
            throw new IllegalStateException(
                "No se puede crear una factura para este contrato en su estado actual");
        }

        Factura factura = new Factura();
        factura.setContrato(contrato);
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaVencimiento);
        factura.setImporte(importe);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setEliminada(false);
        factura.setConceptoFacturado(conceptoFacturado);    
        
   
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
