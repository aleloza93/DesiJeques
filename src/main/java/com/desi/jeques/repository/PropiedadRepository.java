package com.desi.jeques.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.jeques.entity.Propiedad;

import java.util.List;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
    List<Propiedad> findByEliminadaFalse();
    boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);
}