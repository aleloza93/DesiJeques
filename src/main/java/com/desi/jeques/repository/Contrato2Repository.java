package com.desi.jeques.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desi.jeques.entity.Contrato2;
import com.desi.jeques.utilidades.EstadoContrato;

@Repository
public interface Contrato2Repository extends JpaRepository<Contrato2, Long> {
	
	List<Contrato2> findByEstadoAndEliminadoFalse(EstadoContrato estado); 

}
