package com.desi.jeques.service;

import java.math.BigDecimal;
import java.util.List;

import com.desi.jeques.entity.Publicacion;

public interface PublicacionService {

    List<Publicacion> listarConFiltros(Long propiedadId, String ciudad, String estado,
                                        BigDecimal precioMin, BigDecimal precioMax);

    Publicacion buscarPorId(Long id);

    Publicacion guardar(Publicacion publicacion);

    void eliminarLogico(Long id);
}
