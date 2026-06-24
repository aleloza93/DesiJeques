package com.desi.jeques.service;

import com.desi.jeques.entity.Contrato;

import java.util.List;

public interface ContratoService {

    List<Contrato> listarContratosActivos();

    Contrato buscarPorId(Long id);
    
    boolean puedeFacturarse(Contrato contrato);

    Contrato guardar(Contrato contrato);

    void eliminarLogico(Long id);

    
}