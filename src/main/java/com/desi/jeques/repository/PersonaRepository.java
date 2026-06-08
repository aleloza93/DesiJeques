package com.desi.jeques.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.jeques.entity.Persona;

import java.util.List;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findByEliminadaFalse();
}