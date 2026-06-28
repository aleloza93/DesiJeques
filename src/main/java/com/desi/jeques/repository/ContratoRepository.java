package com.desi.jeques.repository;

import com.desi.jeques.entity.Contrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    boolean existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalse(Long propiedadId, String estado);

    boolean existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalseAndIdNot(
            Long propiedadId,
            String estado,
            Long id);
    
    List<Contrato> findByEstadoAndEliminadoFalse(String estado);
    
    
    @Query("""
    		SELECT c
    		FROM Contrato c
    		WHERE c.eliminado = false
    		AND (:propiedadId IS NULL OR c.propiedad.id = :propiedadId)
    		AND (:inquilinoId IS NULL OR c.inquilino.id = :inquilinoId)
    		AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
    		AND (:estado IS NULL OR c.estado = :estado)
    		
    		""")
    List<Contrato> buscarConFiltros(@Param("propiedadId") Long propiedadId,
						            @Param("inquilinoId") Long inquilinoId,
						            @Param("fechaInicio") LocalDate fechaInicio,
						            @Param("estado") String estado);
						}