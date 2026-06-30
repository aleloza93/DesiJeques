package com.desi.jeques.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.entity.Publicacion;
import com.desi.jeques.repository.PublicacionRepository;
import com.desi.jeques.service.PropiedadService;
import com.desi.jeques.service.PublicacionService;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final PropiedadService propiedadService;

    public PublicacionServiceImpl(PublicacionRepository publicacionRepository,
                                   PropiedadService propiedadService) {
        this.publicacionRepository = publicacionRepository;
        this.propiedadService = propiedadService;
    }

    @Override
    public List<Publicacion> listarConFiltros(Long propiedadId, String ciudad, String estado,
                                               BigDecimal precioMin, BigDecimal precioMax) {
        return publicacionRepository.filtrar(propiedadId, ciudad, estado, precioMin, precioMax);
    }

    @Override
    public Publicacion buscarPorId(Long id) {
        return publicacionRepository.findById(id).orElse(null);
    }

    @Override
    public Publicacion guardar(Publicacion publicacion) {

        boolean esNueva = publicacion.getId() == null;

        // La propiedad no puede modificarse una vez creada la publicación
        Publicacion existente = null;
        if (!esNueva) {
            existente = publicacionRepository.findById(publicacion.getId())
                    .orElseThrow(() -> new RuntimeException("La publicación no existe."));
            publicacion.setPropiedad(existente.getPropiedad());
            publicacion.setEliminada(existente.isEliminada());
        }

        // Reglas para poder dejar/poner la publicación como activa
        if ("activa".equals(publicacion.getEstado())) {

            boolean existeOtraActiva = publicacionRepository.existeActivaParaPropiedad(
                    publicacion.getPropiedad().getId(), publicacion.getId());

            if (existeOtraActiva) {
                throw new RuntimeException(
                        "Ya existe otra publicación activa para esta propiedad.");
            }

            Propiedad propiedad = propiedadService.buscarPorId(publicacion.getPropiedad().getId());

            if (propiedad == null) {
                throw new RuntimeException("La propiedad asociada no existe.");
            }

            if (!"disponible".equals(propiedad.getEstadoDisponibilidad())) {
                throw new RuntimeException(
                        "Solo se puede activar una publicación si la propiedad se encuentra disponible.");
            }
        }

        if (esNueva) {
            publicacion.setEliminada(false);
        }

        return publicacionRepository.save(publicacion);
    }

    @Override
    public void eliminarLogico(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La publicación no existe."));

        if (!"activa".equals(publicacion.getEstado())) {
            throw new RuntimeException(
                    "Solo se pueden eliminar publicaciones activas (si ya no corresponde, "
                  + "primero páusela o finalícela).");
        }

        publicacion.setEliminada(true);
        publicacionRepository.save(publicacion);
    }
}
