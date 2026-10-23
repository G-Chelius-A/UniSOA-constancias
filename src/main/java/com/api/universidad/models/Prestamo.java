package com.api.universidad.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="prestamos")
public class Prestamo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_prestamo", nullable=false)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="matricula_estudiante", nullable=false)
    @JsonIgnoreProperties({"inscripciones", "solicitudes"})
    @NotNull(message="Debe indicarse un estudiante")
    private Estudiante estudiante;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_libro", nullable=false)
    @NotNull(message="Debe indicarse un libro")
    private Libro libro;

    @Column(name="fecha_prestamo", nullable=false, updatable=false)
    private LocalDateTime fechaPrestamo;

    @Column(name="fecha_prevista_devolucion")
    private LocalDateTime fechaPrevistaDevolucion;

    @Column(name="fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable=false, length=20)
    private EstadoPrestamo estado = EstadoPrestamo.PRESTADO;

    // Al crear, la fecha de préstamo será en este instante y el estado del préstamo se actualziará a PRESTADO.

    @PrePersist
    public void onCreate() {
        if (fechaPrestamo == null) {
            fechaPrestamo = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoPrestamo.PRESTADO;
        }
    }

    //Getters, setters y constructores.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaPrevistaDevolucion() {
        return fechaPrevistaDevolucion;
    }

    public void setFechaPrevistaDevolucion(LocalDateTime fechaPrevistaDevolucion) {
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}
