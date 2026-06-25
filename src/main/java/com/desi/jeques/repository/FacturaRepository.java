package com.desi.jeques.repository;

import org.springframework.stereotype.Repository;
import com.desi.jeques.entity.Factura;
import com.desi.jeques.utilidades.EstadoFactura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
	// Buscar facturas de un contrato específico
    List<Factura> findByContratoId(Long contratoId);    

    // Solo las no eliminadas
    List<Factura> findByEliminadaFalse();
    
    //Para buscar las facturas modificables
    List<Factura> findByEstadoInAndEliminadaFalse(List<EstadoFactura> estados);
}
