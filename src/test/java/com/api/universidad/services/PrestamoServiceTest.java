package com.api.universidad.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.EstadoPrestamo;
import com.api.universidad.models.Estudiante;
import com.api.universidad.models.Libro;
import com.api.universidad.models.LibroDevuelto;
import com.api.universidad.models.Prestamo;
import com.api.universidad.repositories.IEstudianteRepository;
import com.api.universidad.repositories.ILibroRepository;
import com.api.universidad.repositories.IPrestamoRepository;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {
    @Mock
    private IPrestamoRepository rPrestamo;

    @Mock
    private IEstudianteRepository rEstudiante;

    @Mock
    private ILibroRepository rLibro;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PrestamoService service;

    @Test
    void registrarPrestamoActualizaDisponibilidadYGuardaPrestamo() {
        Estudiante estudiante = new Estudiante();
        estudiante.setMatricula(51L);
        Libro libro = libroDisponible();
        when(rEstudiante.findById(51L)).thenReturn(Optional.of(estudiante));
        when(rLibro.findByIdForUpdate(15L)).thenReturn(Optional.of(libro));
        when(rPrestamo.save(any(Prestamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Prestamo nuevo = new Prestamo();
        nuevo.setEstudiante(estudiante);
        nuevo.setLibro(libro);

        Prestamo guardado = service.registrarPrestamo(nuevo);

        assertFalse(libro.getDisponible());
        assertEquals(EstadoPrestamo.PRESTADO, guardado.getEstado());
        assertNotNull(guardado.getFechaPrestamo());
        verify(rLibro).save(libro);
        verify(rPrestamo).save(nuevo);
    }

    @Test
    void registrarPrestamoDeLibroNoDisponibleDevuelve409() {
        Estudiante estudiante = new Estudiante();
        estudiante.setMatricula(51L);
        Libro libro = libroDisponible();
        libro.setDisponible(false);
        when(rEstudiante.findById(51L)).thenReturn(Optional.of(estudiante));
        when(rLibro.findByIdForUpdate(15L)).thenReturn(Optional.of(libro));

        Prestamo nuevo = new Prestamo();
        nuevo.setEstudiante(estudiante);
        nuevo.setLibro(libro);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
            () -> service.registrarPrestamo(nuevo));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(rPrestamo, never()).save(any(Prestamo.class));
    }

    @Test
    void devolverPrestamoInexistenteDevuelve404() {
        when(rPrestamo.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
            () -> service.devolverLibro(99L));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(eventPublisher, never()).publishEvent(any(Object.class));
    }

    @Test
    void devolverPrestamoActualizaEstadoYDisponibilidadYPublicaEvento() {
        Prestamo prestamo = prestamoActivo();
        Libro libro = prestamo.getLibro();
        libro.setDisponible(false);
        when(rPrestamo.findByIdForUpdate(28L)).thenReturn(Optional.of(prestamo));
        when(rLibro.findByIdForUpdate(15L)).thenReturn(Optional.of(libro));
        when(rPrestamo.save(prestamo)).thenReturn(prestamo);

        Prestamo devuelto = service.devolverLibro(28L);

        assertEquals(EstadoPrestamo.DEVUELTO, devuelto.getEstado());
        assertNotNull(devuelto.getFechaDevolucion());
        assertTrue(libro.getDisponible());
        verify(rLibro).save(libro);
        verify(eventPublisher).publishEvent(any(LibroDevuelto.class));
    }

    @Test
    void devolverPrestamoDevueltoPreviamenteDevuelve409YNoPublicaEvento() {
        Prestamo prestamo = prestamoActivo();
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        when(rPrestamo.findByIdForUpdate(28L)).thenReturn(Optional.of(prestamo));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
            () -> service.devolverLibro(28L));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(eventPublisher, never()).publishEvent(any(Object.class));
    }

    private Libro libroDisponible() {
        Libro libro = new Libro();
        libro.setId(15L);
        libro.setTitulo("Libro de prueba");
        libro.setAutor("Autora de prueba");
        libro.setDisponible(true);
        return libro;
    }

    private Prestamo prestamoActivo() {
        Estudiante estudiante = new Estudiante();
        estudiante.setMatricula(51L);
        Prestamo prestamo = new Prestamo();
        prestamo.setId(28L);
        prestamo.setEstudiante(estudiante);
        prestamo.setLibro(libroDisponible());
        prestamo.setEstado(EstadoPrestamo.PRESTADO);
        return prestamo;
    }
}
