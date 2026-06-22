package com.desi.jeques.repository;

import com.desi.jeques.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    boolean existsByPropiedadIdAndEstado(Long propiedadId, String estado);
    
    boolean existsByPropiedadIdAndEstadoAndIdNot(
            Long propiedadId,
            String estado,
            Long id);
    
   // boolean existsByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);

   // Optional<Contrato> findByDireccionAndCiudadAndEliminadaFalse(String direccion, String ciudad);
}