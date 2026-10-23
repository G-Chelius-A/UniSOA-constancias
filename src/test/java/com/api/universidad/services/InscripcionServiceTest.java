package com.api.universidad.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Inscripcion;
import com.api.universidad.repositories.IEstudianteRepository;
import com.api.universidad.repositories.IInscripcionRepository;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {
    @Mock
    private IInscripcionRepository rInscripcion;

    @Mock
    private IEstudianteRepository rEstudiante;

    @InjectMocks
    private InscripcionService service;

    @Test
    void obtenerCalificacionesDevuelveSoloCalificacionesDeInscripcionesCompletadas() {
        ArrayList<Inscripcion> calificaciones = new ArrayList<>();
        calificaciones.add(new Inscripcion());
        when(rEstudiante.existsById(120L)).thenReturn(true);
        when(rInscripcion.findByEstudiante_MatriculaAndCompletadaTrueAndCalificacionIsNotNull(120L))
            .thenReturn(calificaciones);

        assertEquals(calificaciones, service.obtenerCalificaciones(120L));
        verify(rInscripcion).findByEstudiante_MatriculaAndCompletadaTrueAndCalificacionIsNotNull(120L);
    }

    @Test
    void obtenerCalificacionesSinResultadosDevuelveListaVacia() {
        when(rEstudiante.existsById(120L)).thenReturn(true);
        when(rInscripcion.findByEstudiante_MatriculaAndCompletadaTrueAndCalificacionIsNotNull(120L))
            .thenReturn(new ArrayList<>());

        assertEquals(0, service.obtenerCalificaciones(120L).size());
    }

    @Test
    void obtenerCalificacionesDeEstudianteInexistenteDevuelve404() {
        when(rEstudiante.existsById(120L)).thenReturn(false);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
            () -> service.obtenerCalificaciones(120L));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }
}
