package com.desi.jeques.service;

import com.desi.jeques.entity.Propiedad;

import java.util.List;

public interface PropiedadService {

    List<Propiedad> listarActivas();

    List<Propiedad> listarConFiltros(String direccion, String ciudad, String tipoPropiedad, String estadoDisponibilidad);

    Propiedad buscarPorId(Long id);

    Propiedad guardar(Propiedad propiedad);

    Propiedad guardarDesdeContrato(Propiedad propiedad);

    void eliminarLogico(Long id);

    boolean existeDuplicada(String direccion, String ciudad, Long idActual);
}