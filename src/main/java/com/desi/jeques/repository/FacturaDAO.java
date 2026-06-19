package com.desi.jeques.repository;

import org.springframework.stereotype.Repository;
import com.desi.jeques.entity.Factura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FacturaDAO extends JpaRepository<Factura, Long> {
	// Buscar facturas de un contrato específico
    List<Factura> findByContratoId(Long contratoId);

    // Buscar por estado
    List<Factura> findByEstado(EstadoFactura estado);

    // Solo las no eliminadas
    List<Factura> findByEliminadaFalse();
}
