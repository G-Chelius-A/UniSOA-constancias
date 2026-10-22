package com.api.universidad.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Curso;
import com.api.universidad.models.Estudiante;
import com.api.universidad.models.Inscripcion;
import com.api.universidad.repositories.ICursoRepository;
import com.api.universidad.repositories.IEstudianteRepository;
import com.api.universidad.repositories.IInscripcionRepository;

import jakarta.transaction.Transactional;

@Service
public class InscripcionService {

    @Autowired
    private IInscripcionRepository rInscripcion;

    @Autowired
    private IEstudianteRepository rEstudiante;

    @Autowired
    private ICursoRepository rCurso;

    public ArrayList<Inscripcion> obtenerInscripciones() {
        return (ArrayList<Inscripcion>) rInscripcion.findAll();
    }

    public Optional<Inscripcion> obtenerInscripcionPorId(Long id) {
        return rInscripcion.findById(id);
    }

    public ArrayList<Inscripcion> obtenerPorEstudiante(Long matricula) {
        return rInscripcion.findByEstudiante_Matricula(matricula);
    }

    public ArrayList<Inscripcion> obtenerPorCurso(Long idCurso) {
        return rInscripcion.findByCurso_Id(idCurso);
    }

    @Transactional
    public Inscripcion registrarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getEstudiante() == null || inscripcion.getEstudiante().getMatricula() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicarse la matrícula del estudiante");
        }

        if (inscripcion.getCurso() == null || inscripcion.getCurso().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicarse el ID del curso");
        }

        Estudiante estudiante = rEstudiante.findById(inscripcion.getEstudiante().getMatricula())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el estudiante con matrícula: " + inscripcion.getEstudiante().getMatricula()));

        Curso curso = rCurso.findById(inscripcion.getCurso().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el curso con ID: " + inscripcion.getCurso().getId()));

        if (rInscripcion.existsByEstudiante_MatriculaAndCurso_IdAndPeriodoAndAnio(
                estudiante.getMatricula(), curso.getId(), inscripcion.getPeriodo(), inscripcion.getAnio())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El estudiante ya está inscrito en este curso para el periodo y año seleccionados");
        }

        inscripcion.setEstudiante(estudiante);
        inscripcion.setCurso(curso);

        return rInscripcion.save(inscripcion);
    }

    @Transactional
    public Inscripcion actualizarInscripcion(Long id, Inscripcion detalles) {
        Inscripcion inscripcionExistente = rInscripcion.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la inscripción con ID: " + id));

        if (detalles.getCalificacion() != null) {
            inscripcionExistente.setCalificacion(detalles.getCalificacion());
        }

        if (detalles.getEstado() != null) {
            inscripcionExistente.setEstado(detalles.getEstado());
        }

        if (detalles.getPeriodo() != null) {
            inscripcionExistente.setPeriodo(detalles.getPeriodo());
        }

        if (detalles.getAnio() != null) {
            inscripcionExistente.setAnio(detalles.getAnio());
        }

        if (detalles.getCurso() != null && detalles.getCurso().getId() != null) {
            Curso curso = rCurso.findById(detalles.getCurso().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el curso con ID: " + detalles.getCurso().getId()));
            inscripcionExistente.setCurso(curso);
        }

        if (detalles.getEstudiante() != null && detalles.getEstudiante().getMatricula() != null) {
            Estudiante estudiante = rEstudiante.findById(detalles.getEstudiante().getMatricula())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el estudiante con matrícula: " + detalles.getEstudiante().getMatricula()));
            inscripcionExistente.setEstudiante(estudiante);
        }

        return rInscripcion.save(inscripcionExistente);
    }

    @Transactional
    public void eliminarInscripcion(Long id) {
        if (!rInscripcion.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la inscripción con ID: " + id);
        }
        rInscripcion.deleteById(id);
    }
}
