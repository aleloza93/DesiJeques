package com.desi.jeques.service.impl;

import com.desi.jeques.entity.HistorialEstadoPropiedad;
import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.repository.ContratoRepository;
import com.desi.jeques.repository.HistorialEstadoPropiedadRepository;
import com.desi.jeques.repository.PropiedadRepository;
import com.desi.jeques.service.PropiedadService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    private final PropiedadRepository propiedadRepository;
    private final HistorialEstadoPropiedadRepository historialRepository;
    private final ContratoRepository contratoRepository;

    public PropiedadServiceImpl(PropiedadRepository propiedadRepository,
                                HistorialEstadoPropiedadRepository historialRepository,
                                ContratoRepository contratoRepository) {
        this.propiedadRepository = propiedadRepository;
        this.historialRepository = historialRepository;
        this.contratoRepository = contratoRepository;
    }

    @Override
    public List<Propiedad> listarActivas() {
        return propiedadRepository.findByEliminadaFalse();
    }

    @Override
    public List<Propiedad> listarConFiltros(String direccion, String ciudad, String tipoPropiedad, String estadoDisponibilidad) {
        return propiedadRepository.buscarConFiltros(
                normalizarTexto(direccion),
                normalizarTexto(ciudad),
                normalizarTexto(tipoPropiedad),
                normalizarTexto(estadoDisponibilidad)
        );
    }

    @Override
    public Propiedad buscarPorId(Long id) {
        return propiedadRepository.findById(id).orElse(null);
    }

    @Override
    public Propiedad guardar(Propiedad propiedad) {
        return guardarInterno(propiedad, false);
    }

    @Override
    public Propiedad guardarDesdeContrato(Propiedad propiedad) {
        return guardarInterno(propiedad, true);
    }

    @Override
    public void eliminarLogico(Long id) {
        Propiedad propiedad = buscarPorId(id);

        if (propiedad != null) {
            if (tieneContratoActivo(propiedad.getId())) {
                throw new RuntimeException(
                        "No se puede eliminar la propiedad porque posee un contrato activo.");
            }

            propiedad.setEliminada(true);
            propiedadRepository.save(propiedad);
        }
    }

    @Override
    public boolean existeDuplicada(String direccion, String ciudad, Long idActual) {
        Optional<Propiedad> encontrada =
                propiedadRepository.findByDireccionAndCiudadAndEliminadaFalse(direccion, ciudad);

        if (encontrada.isEmpty()) {
            return false;
        }

        if (idActual == null) {
            return true;
        }

        return !encontrada.get().getId().equals(idActual);
    }

    private Propiedad guardarInterno(Propiedad propiedad, boolean omitirValidacionContratoActivo) {
        boolean esNueva = propiedad.getId() == null;
        String estadoAnterior = null;

        if (esNueva && (propiedad.getEstadoDisponibilidad() == null || propiedad.getEstadoDisponibilidad().isBlank())) {
            propiedad.setEstadoDisponibilidad("disponible");
        }

        if (!esNueva) {
            Propiedad existente = buscarPorId(propiedad.getId());

            if (existente != null) {
                estadoAnterior = existente.getEstadoDisponibilidad();

                if (!omitirValidacionContratoActivo
                        && cambioABloqueadoConContratoActivo(estadoAnterior, propiedad.getEstadoDisponibilidad())
                        && tieneContratoActivo(existente.getId())) {

                    throw new RuntimeException(
                            "No se puede cambiar la propiedad a disponible o inactiva porque posee un contrato activo.");
                }
            }
        }

        Propiedad guardada = propiedadRepository.save(propiedad);

        if (esNueva) {
            registrarHistorial(guardada);
        } else if (estadoAnterior != null
                && !estadoAnterior.equalsIgnoreCase(guardada.getEstadoDisponibilidad())) {
            registrarHistorial(guardada);
        }

        return guardada;
    }

    private boolean tieneContratoActivo(Long propiedadId) {
        return contratoRepository.existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalse(
                propiedadId, "ACTIVO");
    }

    private boolean cambioABloqueadoConContratoActivo(String estadoAnterior, String estadoNuevo) {
        if (estadoNuevo == null) {
            return false;
        }

        boolean pasaADisponible = "disponible".equalsIgnoreCase(estadoNuevo);
        boolean pasaAInactiva = "inactiva".equalsIgnoreCase(estadoNuevo);

        if (!(pasaADisponible || pasaAInactiva)) {
            return false;
        }

        return estadoAnterior == null || !estadoAnterior.equalsIgnoreCase(estadoNuevo);
    }

    private void registrarHistorial(Propiedad propiedad) {
        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
        historial.setPropiedad(propiedad);
        historial.setEstado(propiedad.getEstadoDisponibilidad());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepository.save(historial);
    }

    private String normalizarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}