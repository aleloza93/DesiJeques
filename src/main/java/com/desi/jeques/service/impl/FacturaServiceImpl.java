package com.desi.jeques.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    
    
    
    /***************CREAR FACTURA***************/
    @Override
    @Transactional
    public Factura crearFactura(Long contratoId,
    		String conceptoFacturado,
            LocalDate fechaEmision,
            LocalDate fechaVencimiento,
            BigDecimal importe,
            LocalDate fechaPago,
            MedioPago medioPago,
            BigDecimal importePagado,
            BigDecimal interesPagado) {
    	
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
    
    
    /***************MODIFICAR FACTURA***************/
    
    @Override
    public List<Factura> listarModificables() {
        return facturaRepository.findByEstadoInAndEliminadaFalse(
            List.of(EstadoFactura.PENDIENTE, EstadoFactura.VENCIDA)
        );
    }
    
    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada con id: " + id));
    }
    
    @Override
    @Transactional
    public Factura modificarFactura(Long id, EstadoFactura nuevoEstado,
                                     String conceptoFacturado,
                                     LocalDate fechaEmision,
                                     LocalDate fechaVencimiento,
                                     BigDecimal importe,
                                     LocalDate fechaPago,
                                     MedioPago medioPago,
                                     BigDecimal importePagado,
                                     BigDecimal interes) {

        
        Factura factura = obtenerPorId(id);

        //Verifico si le quieren cambiar el estado anulada o pagada
        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalStateException("No se puede modificar una factura anulada");
        }
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalStateException("No se puede modificar una factura pagada");
        }
        
        //Verificacion de fecha 
        if (fechaVencimiento.isBefore(fechaEmision)) {
            throw new IllegalArgumentException(
                "La fecha de vencimiento debe ser igual o posterior a la de emisión");
        }

        //Verifico que sea valido el cambio de estado
        if (!validarCambioEstado(factura.getEstado(), nuevoEstado)) {
            throw new IllegalStateException(
                "No se puede realizar ese cambio de estado de factura");

        }

        // Verifico que si cambia a PAGADA se ingresen los datos de pago
        if (nuevoEstado == EstadoFactura.PAGADA) {            
            if (fechaPago == null) {
                throw new IllegalArgumentException("La fecha de pago es obligatoria");
            }
            if (medioPago == null) {
                throw new IllegalArgumentException("El medio de pago es obligatorio");
            }
            if (importePagado == null || importePagado.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El importe pagado debe ser positivo");
            }
            factura.setFechaPago(fechaPago);
            factura.setMedio(medioPago);
            factura.setImportePagado(importePagado);
            factura.setInteres(interes);
        } else {            
            factura.setFechaPago(null);
            factura.setMedio(null);
            factura.setImportePagado(null);
            factura.setInteres(null);
        }

        // Si esta VENCIDA no se modifica el importe
        if (nuevoEstado != EstadoFactura.VENCIDA) {
            factura.setImporte(importe);
        }

        // Actualizacion de datos y a la base de datos
        EstadoFactura estadoAnterior = factura.getEstado();
        factura.setConceptoFacturado(conceptoFacturado);
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaVencimiento);
        factura.setEstado(nuevoEstado);

        Factura facturaGuardada = facturaRepository.save(factura);

        // Si cambia el estado, el historial de factura registra
        if (estadoAnterior != nuevoEstado) {
            HistorialEstadoFactura historial = new HistorialEstadoFactura();
            historial.setFactura(facturaGuardada);
            historial.setEstado(nuevoEstado);
            historial.setFechaHora(LocalDateTime.now());
            historialRepository.save(historial);
        }

        return facturaGuardada;
    }
    
    //Se chequea el estado de ambas facturas
    public boolean validarCambioEstado(EstadoFactura estadoActual, EstadoFactura nuevoEstado) {
        if (estadoActual == nuevoEstado) return true;
        if (estadoActual == EstadoFactura.VENCIDA && nuevoEstado == EstadoFactura.PAGADA) return true;
        return false;
    }
    
    /***************ELIMINAR FACTURA***************/
    @Override
    public List<Factura> listarEliminables() {
        return facturaRepository.findByEstadoNotInAndEliminadaFalse(List.of(EstadoFactura.PAGADA));
    } 
    
    @Override
    @Transactional
    public boolean eliminarFactura(Long id) {
    	Factura factura = obtenerPorId(id);    	

        if (factura.isEliminada()) return false;
    	
    	if(factura.getEstado() == EstadoFactura.PAGADA) {
    		System.out.print("Esta queriendo eliminar una factura PAGADA, no deberia hacer eso"); // ver error aca
    		return false;
    	}
    	
    	factura.setEliminada(true);
    	
    	facturaRepository.save(factura);
    	
    	return true;
    }
    
    /***************VER FACTURA***************/
   
    public List<Factura> facturasNoEliminadas(){
    	return facturaRepository.findByEliminadaFalse();
    }
    
    public List<Factura> filtrar(
            Long contratoId,
            Long propiedadId,
            Long inquilinoId,
            EstadoFactura estado,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        List<Factura> facturas = facturaRepository.findByEliminadaFalse();
        
        List<Factura> listaFiltrada = new ArrayList<>();
        
        for(Factura factura : facturas) {
        	boolean bandera = true;
        	
        	if (contratoId != null) {
                if (!factura.getContrato().getId().equals(contratoId)) {
                	bandera = false;
                }
            }
        	
        	if (propiedadId != null) {
                if (!factura.getContrato().getPropiedad().getId().equals(propiedadId)) {
                	bandera = false;
                }
            }
        	
        	if (inquilinoId != null) {
                if (!factura.getContrato().getInquilino().getId().equals(inquilinoId)) {
                    bandera = false;
                }
            }
        	
        	if (estado != null) {
                if (factura.getEstado() != estado) {
                	bandera = false;
                }
            }
        	
        	if (fechaDesde != null) {
                if (factura.getFechaVencimiento().isBefore(fechaDesde)) {
                	bandera = false;
                }
            }
        	
        	if (fechaHasta != null) {
                if (factura.getFechaVencimiento().isAfter(fechaHasta)) {
                	bandera = false;
                }
            }
        	
        	if (bandera) {
        		listaFiltrada.add(factura);
            }
        	
        }
        
        return listaFiltrada;
    }
}
