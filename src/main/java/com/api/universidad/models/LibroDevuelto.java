package com.api.universidad.models;

import java.time.LocalDateTime;

//No es una entidad, es una clase sobre la cual se trabajará un "evento" síncrono (diferente al PagoConfirmado event). Es como un mini "DTO".
public class LibroDevuelto {
    private final Long idPrestamo;
    private final Long idLibro;
    private final Long matriculaEstudiante;
    private final LocalDateTime fechaDevolucion;

    //Constructores, getters y setters.

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
