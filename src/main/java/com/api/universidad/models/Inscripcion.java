package com.api.universidad.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

@Entity
@Table(name="inscripciones")
public class Inscripcion {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_inscripcion", nullable=false)
    private Long id;

    @Column(name="calificacion", precision=3, scale=1)
    @DecimalMin(value="0.0", message="La calificación debe ser al menos 0.0")
    @DecimalMax(value="10.0", message="La calificación no puede ser mayor que 10.0")
    @Digits(integer=2, fraction=1, message="La calificación admite como máximo un decimal")
    private BigDecimal calificacion;

    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;

    @Column(name="periodo", nullable=false)
    @NotNull(message="Debe indicarse un periodo (ENE_JUN / AGO_DIC)")
    private Periodo periodo;

    @Column(name="anio", nullable=false, length=4)
    @NotNull(message="Debe indicarse un año")
    private Integer anio;

    @Column(name="activo", nullable=false)
    private Boolean estado = true;

    @Column(name="completada", nullable=false)
    private Boolean completada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_curso", nullable=false)
    @JsonIgnoreProperties("inscripciones")
    @NotNull(message="Debe indicarse un curso")
    private Curso curso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_estudiante", nullable=false)
    @JsonIgnoreProperties("inscripciones")
    @NotNull(message="Debe indicarse un estudiante")
    private Estudiante estudiante;

    //Asignar fecha de creación al crear una inscripción.

    @PrePersist 
    public void onCreate(){
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = true;
        }
        if (this.completada == null) {
            this.completada = false;
        }
    }

    //Getters, setters y constructores.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(BigDecimal calificacion) {
        this.calificacion = calificacion;
    }

    public Boolean getCompletada() {
        return completada;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public Boolean isEstado() {
        return estado != null ? estado : true;
    }

    public Boolean getEstado() {
        return estado != null ? estado : true;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Inscripcion() {
    }

    public Inscripcion(Long id, BigDecimal calificacion, LocalDateTime fechaCreacion,
            @NotNull(message = "Debe indicarse un periodo") Periodo periodo,
            @NotNull(message = "Debe indicarse un año") Integer anio, Boolean estado,
            @NotNull(message = "Debe indicarse un curso") Curso curso,
            @NotNull(message = "Debe indicarse un estudiante") Estudiante estudiante) {
        this.id = id;
        this.calificacion = calificacion;
        this.fechaCreacion = fechaCreacion;
        this.periodo = periodo;
        this.anio = anio;
        this.estado = estado != null ? estado : true;
        this.curso = curso;
        this.estudiante = estudiante;
    }
    
}
