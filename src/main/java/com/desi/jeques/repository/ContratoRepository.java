package com.desi.jeques.repository;

import com.desi.jeques.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    boolean existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalse(Long propiedadId, String estado);

    boolean existsByPropiedadIdAndEstadoIgnoreCaseAndEliminadoFalseAndIdNot(
            Long propiedadId,
            String estado,
            Long id);
}