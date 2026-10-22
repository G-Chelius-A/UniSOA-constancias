package com.api.universidad.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Estudiante;
import com.api.universidad.repositories.IEstudianteRepository;

import jakarta.transaction.Transactional;

@Service
public class EstudianteService {

    @Autowired
    private IEstudianteRepository rEstudiante;

    public ArrayList<Estudiante> obtenerEstudiantes() {
        return (ArrayList<Estudiante>) rEstudiante.findAll();
    }

    public Optional<Estudiante> obtenerEstudiantePorMatricula(Long matricula) {
        return rEstudiante.findById(matricula);
    }

    @Transactional
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        if (estudiante.getEmail() != null && rEstudiante.existsByEmail(estudiante.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un estudiante registrado con el correo: " + estudiante.getEmail());
        }
        return rEstudiante.save(estudiante);
    }

    @Transactional
    public Estudiante actualizarEstudiante(Long matricula, Estudiante estudianteDetalles) {
        Estudiante estudianteExistente = rEstudiante.findById(matricula)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el estudiante con matrícula: " + matricula));

        if (estudianteDetalles.getEmail() != null 
                && !estudianteDetalles.getEmail().equalsIgnoreCase(estudianteExistente.getEmail())
                && rEstudiante.existsByEmail(estudianteDetalles.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un estudiante registrado con el correo: " + estudianteDetalles.getEmail());
        }

        estudianteExistente.setNombre(estudianteDetalles.getNombre());
        estudianteExistente.setEmail(estudianteDetalles.getEmail());

        return rEstudiante.save(estudianteExistente);
    }

    @Transactional
    public void eliminarEstudiante(Long matricula) {
        if (!rEstudiante.existsById(matricula)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el estudiante con matrícula: " + matricula);
        }
        rEstudiante.deleteById(matricula);
    }
}
