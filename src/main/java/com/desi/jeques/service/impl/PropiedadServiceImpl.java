package com.desi.jeques.service.impl;

import com.desi.jeques.entity.HistorialEstadoPropiedad;
import com.desi.jeques.entity.Propiedad;
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

    public PropiedadServiceImpl(PropiedadRepository propiedadRepository,
                                HistorialEstadoPropiedadRepository historialRepository) {
        this.propiedadRepository = propiedadRepository;
        this.historialRepository = historialRepository;
    }

    @Override
    public List<Propiedad> listarActivas() {
        return propiedadRepository.findByEliminadaFalse();
    }

    @Override
    public Propiedad buscarPorId(Long id) {
        return propiedadRepository.findById(id).orElse(null);
    }

    @Override
    public Propiedad guardar(Propiedad propiedad) {
        boolean esNueva = propiedad.getId() == null;
        String estadoAnterior = null;

        if (esNueva && (propiedad.getEstadoDisponibilidad() == null || propiedad.getEstadoDisponibilidad().isBlank())) {
            propiedad.setEstadoDisponibilidad("disponible");
        }

        if (!esNueva) {
            Propiedad existente = buscarPorId(propiedad.getId());
            if (existente != null) {
                estadoAnterior = existente.getEstadoDisponibilidad();
            }
        }

        Propiedad guardada = propiedadRepository.save(propiedad);

        if (esNueva) {
            registrarHistorial(guardada);
        } else if (estadoAnterior != null && !estadoAnterior.equalsIgnoreCase(guardada.getEstadoDisponibilidad())) {
            registrarHistorial(guardada);
        }

        return guardada;
    }

    @Override
    public void eliminarLogico(Long id) {
        Propiedad propiedad = buscarPorId(id);
        if (propiedad != null) {
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

    private void registrarHistorial(Propiedad propiedad) {
        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
        historial.setPropiedad(propiedad);
        historial.setEstado(propiedad.getEstadoDisponibilidad());
        historial.setFechaCambio(LocalDateTime.now());
        historialRepository.save(historial);
    }
}