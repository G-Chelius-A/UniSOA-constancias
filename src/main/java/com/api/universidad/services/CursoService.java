package com.api.universidad.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Curso;
import com.api.universidad.repositories.ICursoRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {

    @Autowired
    private ICursoRepository rCurso;

    public ArrayList<Curso> obtenerCursos() {
        return (ArrayList<Curso>) rCurso.findAll();
    }

    public Optional<Curso> obtenerCursoPorId(Long id) {
        return rCurso.findById(id);
    }

    @Transactional
    public Curso guardarCurso(Curso curso) {
        return rCurso.save(curso);
    }

    @Transactional
    public Curso actualizarCurso(Long id, Curso cursoDetalles) {
        Curso cursoExistente = rCurso.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el curso con ID: " + id));

        cursoExistente.setNombre(cursoDetalles.getNombre());

        return rCurso.save(cursoExistente);
    }

    @Transactional
    public void eliminarCurso(Long id) {
        if (!rCurso.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el curso con ID: " + id);
        }
        rCurso.deleteById(id);
    }
}
