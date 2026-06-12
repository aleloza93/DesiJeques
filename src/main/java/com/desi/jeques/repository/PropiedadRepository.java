package com.desi.jeques.repository;

import com.desi.jeques.entity.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadaFalse();

    boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

    Optional<Propiedad> findByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);
}