package com.desi.jeques.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.HistorialEstadoContrato;
import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.repository.ContratoRepository;
import com.desi.jeques.repository.HistorialEstadoContratoRepository;
import com.desi.jeques.service.ContratoService;
import com.desi.jeques.service.PropiedadService;

@Service
public class ContratoServiceImpl  implements ContratoService{
	
	private final ContratoRepository contratoRepository;
	private final HistorialEstadoContratoRepository historialContratoRepository;
	private final PropiedadService propiedadService;

	public ContratoServiceImpl(ContratoRepository contratoRepository,
			HistorialEstadoContratoRepository historialContratoRepository,
			PropiedadService propiedadService) {
		super();
		this.contratoRepository = contratoRepository;
		this.historialContratoRepository = historialContratoRepository;
		this.propiedadService = propiedadService;
	}


	@Override
	public List<Contrato> listarContratosActivos() {
		return contratoRepository.findByEliminadoFalse();
	}

	@Override
	public Contrato buscarPorId(Long id) {
		return contratoRepository.findById(id).orElse(null);
	}
	
	//Funcion para Colox
	@Override
	public boolean puedeFacturarse(Contrato contrato) {
        if (contrato.getEliminado()) {
            return false;
        }
        return "ACTIVO".equalsIgnoreCase(contrato.getEstado());
    }

	@Override
	public Contrato guardar(Contrato contrato) {

	    boolean esNuevo = contrato.getId() == null;

	    String estadoAnterior = null;

	    if (!esNuevo) {
	        Contrato contratoExistente = buscarPorId(contrato.getId());

	        if (contratoExistente != null) {
	            estadoAnterior = contratoExistente.getEstado();
	        }
	    }

	    Propiedad propiedad = propiedadService.buscarPorId(
	            contrato.getPropiedad().getId());

	    /* =========================
	       REGLAS DE ALTA
	       ========================= */

	    if (esNuevo) {

	        if ("ACTIVO".equalsIgnoreCase(contrato.getEstado())
	                && !"DISPONIBLE".equalsIgnoreCase(propiedad.getEstadoDisponibilidad())) {

	            throw new RuntimeException(
	                    "No se puede crear un contrato activo porque la propiedad no está disponible.");
	        }

	        if ("ACTIVO".equalsIgnoreCase(contrato.getEstado())
	                && contratoRepository.existsByPropiedadIdAndEstado(
	                        propiedad.getId(),
	                        "ACTIVO")) {

	            throw new RuntimeException(
	                    "La propiedad ya posee un contrato activo.");
	        }

	        if ("ACTIVO".equalsIgnoreCase(contrato.getEstado())) {

	            propiedad.setEstadoDisponibilidad("ALQUILADA");
	            propiedadService.guardar(propiedad);
	        }
	    }

	    /* =========================
	       REGLAS DE EDICIÓN
	       ========================= */

	    else {

	        String estadoNuevo = contrato.getEstado();

	        /* No activar si la propiedad no está disponible */
	        if (!"ACTIVO".equalsIgnoreCase(estadoAnterior)
	                && "ACTIVO".equalsIgnoreCase(estadoNuevo)
	                && !"DISPONIBLE".equalsIgnoreCase(propiedad.getEstadoDisponibilidad())) {

	            throw new RuntimeException(
	                    "No se puede activar el contrato porque la propiedad no está disponible.");
	        }

	        /* No activar si existe otro contrato activo */
	        if (!"ACTIVO".equalsIgnoreCase(estadoAnterior)
	                && "ACTIVO".equalsIgnoreCase(estadoNuevo)
	                && contratoRepository.existsByPropiedadIdAndEstadoAndIdNot(
	                        propiedad.getId(),
	                        "ACTIVO",
	                        contrato.getId())) {

	            throw new RuntimeException(
	                    "La propiedad ya posee otro contrato activo.");
	        }

	        /* Cambios de estado permitidos */

	        if ("BORRADOR".equalsIgnoreCase(estadoAnterior)
	                && !("BORRADOR".equalsIgnoreCase(estadoNuevo)
	                    || "ACTIVO".equalsIgnoreCase(estadoNuevo))) {

	            throw new RuntimeException(
	                    "Un contrato borrador sólo puede pasar a activo.");
	        }

	        if ("FINALIZADO".equalsIgnoreCase(estadoAnterior)
	                || "RESCINDIDO".equalsIgnoreCase(estadoAnterior)) {

	            if (!estadoAnterior.equalsIgnoreCase(estadoNuevo)) {

	                throw new RuntimeException(
	                        "No se puede modificar el estado de un contrato finalizado o rescindido.");
	            }
	        }

	        if ("ACTIVO".equalsIgnoreCase(estadoAnterior)
	                && !("ACTIVO".equalsIgnoreCase(estadoNuevo)
	                    || "FINALIZADO".equalsIgnoreCase(estadoNuevo)
	                    || "RESCINDIDO".equalsIgnoreCase(estadoNuevo))) {

	            throw new RuntimeException(
	                    "Cambio de estado no permitido.");
	        }

	        /* Activo -> propiedad alquilada */

	        if (!"ACTIVO".equalsIgnoreCase(estadoAnterior)
	                && "ACTIVO".equalsIgnoreCase(estadoNuevo)) {

	            propiedad.setEstadoDisponibilidad("ALQUILADA");
	            propiedadService.guardar(propiedad);
	        }

	        /* Finalizado o rescindido -> disponible */

	        if ("ACTIVO".equalsIgnoreCase(estadoAnterior)
	                && ("FINALIZADO".equalsIgnoreCase(estadoNuevo)
	                    || "RESCINDIDO".equalsIgnoreCase(estadoNuevo))) {

	            propiedad.setEstadoDisponibilidad("DISPONIBLE");
	            propiedadService.guardar(propiedad);
	        }
	    }

	    Contrato guardado = contratoRepository.save(contrato);

	    if (esNuevo) {
	        registrarHistorial(guardado);
	    } else if (estadoAnterior != null
	            && !estadoAnterior.equalsIgnoreCase(guardado.getEstado())) {

	        registrarHistorial(guardado);
	    }

	    return guardado;
	}

	@Override
	public void eliminarLogico(Long id) {
		   Contrato contrato = buscarPorId(id);
		   
	       if (contrato != null) {
	        	
		       	/* Lógica para ver si está en estado "borrador" para poder eliminarlo. Sino no puedo */
		 		if (!"borrador".equalsIgnoreCase(contrato.getEstado())) {
		 		        throw new IllegalArgumentException(
		 		                "Solo pueden eliminarse contratos en estado borrador.");
		 		}
	        	
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
