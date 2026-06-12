package com.desi.jeques.service;

import com.desi.jeques.entity.Propiedad;

import java.util.List;

public interface PropiedadService {

    List<Propiedad> listarActivas();

    Propiedad buscarPorId(Long id);

    Propiedad guardar(Propiedad propiedad);

    void eliminarLogico(Long id);

    boolean existeDuplicada(String direccion, String ciudad, Long idActual);
}