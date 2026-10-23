package com.api.universidad.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Libro;
import com.api.universidad.repositories.ILibroRepository;

import jakarta.transaction.Transactional;

/*
    Este es un servicio de entidad para CRUD de libros.
*/

@Service
public class LibroService {

    @Autowired
    private ILibroRepository rLibro;

    public ArrayList<Libro> obtenerLibros() {
        return (ArrayList<Libro>) rLibro.findAll();
    }

    public Optional<Libro> obtenerLibroPorId(Long id) {
        return rLibro.findById(id);
    }

    public Optional<Libro> obtenerLibroPorIsbn(String isbn) {
        return rLibro.findByIsbn(isbn);
    }

    @Transactional
    public Libro guardarLibro(Libro libro) {
        if (libro.getIsbn() != null && !libro.getIsbn().isBlank() && rLibro.existsByIsbn(libro.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un libro registrado con el ISBN: " + libro.getIsbn());
        }
        if (libro.getDisponible() == null) {
            libro.setDisponible(true);
        }
        return rLibro.save(libro);
    }

    @Transactional
    public Libro actualizarLibro(Long id, Libro libroDetalles) {
        Libro libroExistente = rLibro.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro con ID: " + id));

        if (libroDetalles.getIsbn() != null 
                && !libroDetalles.getIsbn().isBlank()
                && !libroDetalles.getIsbn().equalsIgnoreCase(libroExistente.getIsbn())
                && rLibro.existsByIsbn(libroDetalles.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un libro registrado con el ISBN: " + libroDetalles.getIsbn());
        }

        libroExistente.setTitulo(libroDetalles.getTitulo());
        libroExistente.setAutor(libroDetalles.getAutor());
        libroExistente.setIsbn(libroDetalles.getIsbn());
        if (libroDetalles.getDisponible() != null) {
            libroExistente.setDisponible(libroDetalles.getDisponible());
        }

        return rLibro.save(libroExistente);
    }

    @Transactional
    public void eliminarLibro(Long id) {
        if (!rLibro.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro con ID: " + id);
        }
        rLibro.deleteById(id);
    }
}
