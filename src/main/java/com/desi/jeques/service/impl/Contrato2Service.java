
//ESTA CREADO ASI PARA QUE SIRVA A FACTURA MOMENTANEAMENTE!!!


package com.desi.jeques.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.jeques.entity.Contrato2;
import com.desi.jeques.repository.Contrato2Repository;
import com.desi.jeques.utilidades.EstadoContrato;



@Service
public class Contrato2Service {

    @Autowired
    private Contrato2Repository contratoRepository;

    public Contrato2 obtenerPorId(Long id) {
        return contratoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }

    public boolean puedeFacturarse(Contrato2 contrato) {
        if (contrato.isEliminado()) {
            return false;
        }
        return contrato.getEstado() == EstadoContrato.ACTIVO;
    }
    
    public List<Contrato2> obtenerActivos() {
        return contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO);
    }

}
