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

import com.api.universidad.models.Estudiante;
import com.api.universidad.services.EstudianteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService sEstudiante;

    @GetMapping
    public ResponseEntity<ArrayList<Estudiante>> obtenerTodos() {
        return ResponseEntity.ok(sEstudiante.obtenerEstudiantes());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<Estudiante> obtenerPorMatricula(@PathVariable Long matricula) {
        Estudiante estudiante = sEstudiante.obtenerEstudiantePorMatricula(matricula)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el estudiante con matrícula: " + matricula));
        return ResponseEntity.ok(estudiante);
    }

    @PostMapping
    public ResponseEntity<Estudiante> guardar(@Valid @RequestBody Estudiante estudiante) {
        Estudiante guardado = sEstudiante.guardarEstudiante(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{matricula}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long matricula, @Valid @RequestBody Estudiante estudiante) {
        Estudiante actualizado = sEstudiante.actualizarEstudiante(matricula, estudiante);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> eliminar(@PathVariable Long matricula) {
        sEstudiante.eliminarEstudiante(matricula);
        return ResponseEntity.noContent().build();
    }
}
