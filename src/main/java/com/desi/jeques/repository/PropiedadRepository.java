package com.desi.jeques.repository;

import com.desi.jeques.entity.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadaFalse();

    boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

    Optional<Propiedad> findByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

    @Query("""
        SELECT p
        FROM Propiedad p
        WHERE p.eliminada = false
          AND (:direccion IS NULL OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
          AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
          AND (:tipoPropiedad IS NULL OR p.tipoPropiedad = :tipoPropiedad)
          AND (:estadoDisponibilidad IS NULL OR p.estadoDisponibilidad = :estadoDisponibilidad)
    """)
    List<Propiedad> buscarConFiltros(@Param("direccion") String direccion,
                                     @Param("ciudad") String ciudad,
                                     @Param("tipoPropiedad") String tipoPropiedad,
                                     @Param("estadoDisponibilidad") String estadoDisponibilidad);
}