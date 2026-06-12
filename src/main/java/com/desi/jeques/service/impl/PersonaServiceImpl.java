package com.desi.jeques.service.impl;

import com.desi.jeques.entity.Persona;
import com.desi.jeques.repository.PersonaRepository;
import com.desi.jeques.service.PersonaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public List<Persona> listarActivas() {
        return personaRepository.findByEliminadaFalse();
    }
}