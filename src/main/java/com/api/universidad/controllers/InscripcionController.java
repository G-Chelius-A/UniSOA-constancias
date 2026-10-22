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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Inscripcion;
import com.api.universidad.services.InscripcionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService sInscripcion;

    @GetMapping
    public ResponseEntity<ArrayList<Inscripcion>> obtenerTodas(
            @RequestParam(required = false) Long matricula,
            @RequestParam(required = false) Long cursoId) {
        if (matricula != null) {
            return ResponseEntity.ok(sInscripcion.obtenerPorEstudiante(matricula));
        }
        if (cursoId != null) {
            return ResponseEntity.ok(sInscripcion.obtenerPorCurso(cursoId));
        }
        return ResponseEntity.ok(sInscripcion.obtenerInscripciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> obtenerPorId(@PathVariable Long id) {
        Inscripcion inscripcion = sInscripcion.obtenerInscripcionPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la inscripción con ID: " + id));
        return ResponseEntity.ok(inscripcion);
    }

    @PostMapping
    public ResponseEntity<Inscripcion> guardar(@Valid @RequestBody Inscripcion inscripcion) {
        Inscripcion guardada = sInscripcion.registrarInscripcion(inscripcion);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> actualizar(@PathVariable Long id, @RequestBody Inscripcion inscripcion) {
        Inscripcion actualizada = sInscripcion.actualizarInscripcion(id, inscripcion);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sInscripcion.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
