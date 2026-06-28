package com.desi.jeques.service;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.Propiedad;

import java.time.LocalDate;
import java.util.List;

public interface ContratoService {

    List<Contrato> listarContratosActivos();
    
    List<Contrato> listarConFiltros(Long propiedadId, Long inquilinoId, LocalDate fechaInicio, String estado);

    Contrato buscarPorId(Long id);
    
    boolean puedeFacturarse(Contrato contrato);

    Contrato guardar(Contrato contrato);

    void eliminarLogico(Long id);
    
    List<Contrato> obtenerActivos();
    
}