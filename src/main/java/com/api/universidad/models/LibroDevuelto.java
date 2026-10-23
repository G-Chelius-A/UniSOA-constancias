package com.api.universidad.models;

import java.time.LocalDateTime;

public class LibroDevuelto {
    private final Long idPrestamo;
    private final Long idLibro;
    private final Long matriculaEstudiante;
    private final LocalDateTime fechaDevolucion;

    public LibroDevuelto(Long idPrestamo, Long idLibro, Long matriculaEstudiante,
            LocalDateTime fechaDevolucion) {
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
        this.matriculaEstudiante = matriculaEstudiante;
        this.fechaDevolucion = fechaDevolucion;
    }

    public Long getIdPrestamo() {
        return idPrestamo;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public Long getMatriculaEstudiante() {
        return matriculaEstudiante;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }
}
