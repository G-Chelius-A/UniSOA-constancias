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

import com.api.universidad.models.Libro;
import com.api.universidad.services.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService sLibro;

    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) String isbn) {
        if (isbn != null && !isbn.isBlank()) {
            Libro libro = sLibro.obtenerLibroPorIsbn(isbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro con ISBN: " + isbn));
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.ok(sLibro.obtenerLibros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        Libro libro = sLibro.obtenerLibroPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro con ID: " + id));
        return ResponseEntity.ok(libro);
    }

    @PostMapping
    public ResponseEntity<Libro> guardar(@Valid @RequestBody Libro libro) {
        Libro guardado = sLibro.guardarLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        Libro actualizado = sLibro.actualizarLibro(id, libro);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sLibro.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}
