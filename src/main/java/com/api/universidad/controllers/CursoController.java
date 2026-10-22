package com.api.universidad.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Curso;
import com.api.universidad.services.CursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService sCurso;

    @GetMapping
    public ResponseEntity<ArrayList<Curso>> obtenerTodos() {
        return ResponseEntity.ok(sCurso.obtenerCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        Curso curso = sCurso.obtenerCursoPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el curso con ID: " + id));
        return ResponseEntity.ok(curso);
    }

    @PostMapping
    public ResponseEntity<Curso> guardar(@Valid @RequestBody Curso curso) {
        Curso guardado = sCurso.guardarCurso(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @Valid @RequestBody Curso curso) {
        Curso actualizado = sCurso.actualizarCurso(id, curso);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sCurso.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}
