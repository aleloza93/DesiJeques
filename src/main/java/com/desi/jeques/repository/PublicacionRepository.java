package com.desi.jeques.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.jeques.entity.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query("SELECT p FROM Publicacion p WHERE p.eliminada = false "
         + "AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId) "
         + "AND (:ciudad IS NULL OR LOWER(p.propiedad.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) "
         + "AND (:estado IS NULL OR p.estado = :estado) "
         + "AND (:precioMin IS NULL OR p.precioMensual >= :precioMin) "
         + "AND (:precioMax IS NULL OR p.precioMensual <= :precioMax) "
         + "ORDER BY p.id DESC")
    List<Publicacion> filtrar(@Param("propiedadId") Long propiedadId,
                               @Param("ciudad") String ciudad,
                               @Param("estado") String estado,
                               @Param("precioMin") BigDecimal precioMin,
                               @Param("precioMax") BigDecimal precioMax);

    @Query("SELECT COUNT(p) > 0 FROM Publicacion p WHERE p.propiedad.id = :propiedadId "
         + "AND p.estado = 'activa' AND p.eliminada = false "
         + "AND (:excludeId IS NULL OR p.id <> :excludeId)")
    boolean existeActivaParaPropiedad(@Param("propiedadId") Long propiedadId,
                                       @Param("excludeId") Long excludeId);
}
