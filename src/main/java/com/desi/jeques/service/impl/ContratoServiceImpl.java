package com.desi.jeques.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.HistorialEstadoContrato;

import com.desi.jeques.repository.ContratoRepository;
import com.desi.jeques.repository.HistorialEstadoContratoRepository;
import com.desi.jeques.service.ContratoService;

@Service
public class ContratoServiceImpl  implements ContratoService{
	
	private final ContratoRepository contratoRepository;
	private final HistorialEstadoContratoRepository historialContratoRepository;

	public ContratoServiceImpl(ContratoRepository contratoRepository,
			HistorialEstadoContratoRepository historialContratoRepository) {
		super();
		this.contratoRepository = contratoRepository;
		this.historialContratoRepository = historialContratoRepository;
	}


	@Override
	public List<Contrato> listarContratosActivos() {
		return contratoRepository.findByEliminadoFalse();
	}

	@Override
	public Contrato buscarPorId(Long id) {
		return contratoRepository.findById(id).orElse(null);
	}

	@Override
	public Contrato guardar(Contrato contrato) {
        boolean esNuevo = contrato.getId() == null;
        String estadoAnterior = null;

        if (esNuevo && (contrato.getEstado() == null || contrato.getEstado().isBlank())) {
        	contrato.setEstado("Activo");
        }

        if (!esNuevo) {
            Contrato contratoExistente = buscarPorId(contrato.getId());
            if (contratoExistente != null) {
                estadoAnterior = contratoExistente.getEstado();
            }
        }

        Contrato guardada = contratoRepository.save(contrato);

        if (esNuevo) {
            registrarHistorial(guardada);
        } else if (estadoAnterior != null && !estadoAnterior.equalsIgnoreCase(guardada.getEstado())) {
            registrarHistorial(guardada);
        }

        return guardada;
    } 

	@Override
	public void eliminarLogico(Long id) {
		   Contrato contrato = buscarPorId(id);
	        if (contrato != null) {
	        	contrato.setEliminado(true);
	        	contratoRepository.save(contrato);
	        }
		
	}

    private void registrarHistorial(Contrato contrato) {
        HistorialEstadoContrato historial = new HistorialEstadoContrato();
        historial.setContrato(contrato);
        historial.setEstado(contrato.getEstado());
        historial.setFechaCambio(LocalDateTime.now());
        historialContratoRepository.save(historial);
    }
	
	
	

}
