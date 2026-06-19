package com.desi.jeques.repository;

import org.springframework.stereotype.Repository;
import com.desi.jeques.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FacturaDAO extends JpaRepository<Factura, Long> {

}
