package com.desi.jeques.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.HistorialEstadoContrato;
import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.repository.ContratoRepository;
import com.desi.jeques.repository.HistorialEstadoContratoRepository;
import com.desi.jeques.service.ContratoService;
import com.desi.jeques.service.PropiedadService;

@Service
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final HistorialEstadoContratoRepository historialContratoRepository;
    private final PropiedadService propiedadService;

    public ContratoServiceImpl(ContratoRepository contratoRepository,
                               HistorialEstadoContratoRepository historialContratoRepository,
                               PropiedadService propiedadService) {
        this.contratoRepository = contratoRepository;
        this.historialContratoRepository = historialContratoRepository;
        this.propiedadService = propiedadService;
    }

    @Override
    public List<Contrato> listarContratosActivos() {
        // return contratoRepository.findByEliminadoFalse();
    	return contratoRepository.findByEstadoAndEliminadoFalse("Activo");
    }
    
    
    //Busqueda para colox
    @Override
    public List<Contrato> obtenerActivos() {
        return contratoRepository.findByEstadoAndEliminadoFalse("Activo");
    }

    @Override
    public Contrato buscarPorId(Long id) {
        return contratoRepository.findById(id).orElse(null);
    }

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

        if (esNuevo) {

            if ("ACTIVO".equalsIgnoreCase(contrato.getEstado())
                    && !"DISPONIBLE".equalsIgnoreCase(propiedad.getEstadoDisponibilidad())) {

                throw new RuntimeException(
                        "No se puede crear un contrato activo porque la propiedad no está disponible.");
            }

            if ("ACTIVO".equalsIgnoreCase(contrato.getEstado())
                    && contratoRepository.existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalse(
                            propiedad.getId(),
                            "ACTIVO")) {

                throw new RuntimeException(
                        "La propiedad ya posee un contrato activo.");
            }

            if ("ACTIVO".equalsIgnoreCase(contrato.getEstado())) {
                propiedad.setEstadoDisponibilidad("ALQUILADA");
                propiedadService.guardarDesdeContrato(propiedad);
            }
        } else {

            String estadoNuevo = contrato.getEstado();

            if (!"ACTIVO".equalsIgnoreCase(estadoAnterior)
                    && "ACTIVO".equalsIgnoreCase(estadoNuevo)
                    && !"DISPONIBLE".equalsIgnoreCase(propiedad.getEstadoDisponibilidad())) {

                throw new RuntimeException(
                        "No se puede activar el contrato porque la propiedad no está disponible.");
            }

            if (!"ACTIVO".equalsIgnoreCase(estadoAnterior)
                    && "ACTIVO".equalsIgnoreCase(estadoNuevo)
                    && contratoRepository.existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalseAndIdNot(
                            propiedad.getId(),
                            "ACTIVO",
                            contrato.getId())) {

                throw new RuntimeException(
                        "La propiedad ya posee otro contrato activo.");
            }

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

            if (!"ACTIVO".equalsIgnoreCase(estadoAnterior)
                    && "ACTIVO".equalsIgnoreCase(estadoNuevo)) {

                propiedad.setEstadoDisponibilidad("ALQUILADA");
                propiedadService.guardarDesdeContrato(propiedad);
            }

            if ("ACTIVO".equalsIgnoreCase(estadoAnterior)
                    && ("FINALIZADO".equalsIgnoreCase(estadoNuevo)
                    || "RESCINDIDO".equalsIgnoreCase(estadoNuevo))) {

                propiedad.setEstadoDisponibilidad("DISPONIBLE");
                propiedadService.guardarDesdeContrato(propiedad);
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

    @Override
    public List<Contrato> listarConFiltros(Long propiedadId,
                                           Long inquilinoId,
                                           LocalDate fechaInicio,
                                           String estado) {

        List<Contrato> lista = contratoRepository.buscarConFiltros(
                propiedadId,
                inquilinoId,
                fechaInicio,
                estado);

        System.out.println("Encontrados: " + lista.size());

        for (Contrato c : lista) {
            System.out.println(
                "Contrato " + c.getId() +
                " Propiedad=" + c.getPropiedad().getId() +
                " Inquilino=" + c.getInquilino().getId());
        }

        return lista;
    }
}