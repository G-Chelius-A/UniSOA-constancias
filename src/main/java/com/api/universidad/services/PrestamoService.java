package com.api.universidad.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.EstadoPrestamo;
import com.api.universidad.models.Estudiante;
import com.api.universidad.models.Libro;
import com.api.universidad.models.LibroDevuelto;
import com.api.universidad.models.Prestamo;
import com.api.universidad.repositories.IEstudianteRepository;
import com.api.universidad.repositories.ILibroRepository;
import com.api.universidad.repositories.IPrestamoRepository;

import jakarta.transaction.Transactional;

@Service
public class PrestamoService {
    @Autowired
    private IPrestamoRepository rPrestamo;

    @Autowired
    private IEstudianteRepository rEstudiante;

    @Autowired
    private ILibroRepository rLibro;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Prestamo registrarPrestamo(Prestamo prestamo) {
        if (prestamo.getEstudiante() == null || prestamo.getEstudiante().getMatricula() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Debe indicarse la matrícula del estudiante");
        }
        if (prestamo.getLibro() == null || prestamo.getLibro().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Debe indicarse el ID del libro");
        }

        Long matricula = prestamo.getEstudiante().getMatricula();
        Estudiante estudiante = rEstudiante.findById(matricula)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No se encontró el estudiante con matrícula: " + matricula));

        Long idLibro = prestamo.getLibro().getId();
        // Concurrencia crítica: se bloquea la fila del libro (SELECT ... FOR UPDATE)
        // para garantizar que dos solicitudes simultáneas no presten el mismo ejemplar
        Libro libro = rLibro.findByIdForUpdate(idLibro)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No se encontró el libro con ID: " + idLibro));

        // Validación de disponibilidad
        if (!libro.getDisponible()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "El libro no está disponible para préstamo");
        }

        prestamo.setId(null);
        prestamo.setEstudiante(estudiante);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setFechaDevolucion(null);
        prestamo.setEstado(EstadoPrestamo.PRESTADO);
        // Actualizar disponibilidad del ejemplar a no disponible
        libro.setDisponible(false);

        rLibro.save(libro);
        return rPrestamo.save(prestamo);
    }

    @Transactional
    public Prestamo devolverLibro(Long idPrestamo) {
        Prestamo prestamo = rPrestamo.findByIdForUpdate(idPrestamo)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No se ha encontrado el préstamo con ID: " + idPrestamo));

        if (prestamo.getEstado() != EstadoPrestamo.PRESTADO || prestamo.getFechaDevolucion() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "El préstamo ya fue devuelto");
        }

        Long idLibro = prestamo.getLibro().getId();
        // Bloqueo pesimista para evitar estados inconsistentes durante la devolución
        Libro libro = rLibro.findByIdForUpdate(idLibro)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No se encontró el libro con ID: " + idLibro));

        LocalDateTime fechaDevolucion = LocalDateTime.now();
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        // El libro vuelve a estar disponible para nuevos préstamos
        libro.setDisponible(true);

        rLibro.save(libro);
        Prestamo guardado = rPrestamo.save(prestamo);

        // Notificar evento de devolución a otros componentes interesados
        eventPublisher.publishEvent(new LibroDevuelto(
            guardado.getId(),
            libro.getId(),
            guardado.getEstudiante().getMatricula(),
            fechaDevolucion
        ));
        return guardado;
    }
}
