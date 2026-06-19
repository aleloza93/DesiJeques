package com.desi.jeques.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.jeques.entity.Factura;
import com.desi.jeques.repository.FacturaDAO;
import com.desi.jeques.service.FacturaService;

@Service
public class FacturaServiceImpl implements FacturaService {
	
	@Autowired
    private FacturaDAO facturaRepository;

    public List<Factura> obtenerTodas() {
        return facturaRepository.findByEliminadaFalse();
    }

    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    public Factura guardar(Factura factura) {
        return facturaRepository.save(factura);
    }

    // Eliminado lógico: no borra de la BD
    public void eliminar(Long id) {
        Factura f = obtenerPorId(id);
        f.setEliminada(true);
        facturaRepository.save(f);
    }
}
