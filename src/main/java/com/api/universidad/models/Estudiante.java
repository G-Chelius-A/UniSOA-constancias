package com.api.universidad.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * Estudiante
 */

@Entity 
@Table(name="estudiantes")
public class Estudiante {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="matricula", nullable=false)
    private Long matricula;

    @Column(name="nombre", nullable=false, length=100)
    @NotNull(message="El nombre no debe ser nulo")
    private String nombre;
    
    @Column(name="email", nullable=false)
    @Email(message="El email debe ser válido")
    @NotNull(message="El email no debe ser nulo")
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JsonIgnoreProperties("estudiante")
    private List<Inscripcion> inscripciones = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JsonIgnoreProperties("estudiante")
    private List<Solicitud> solicitudes = new ArrayList<>();

    // Getters, setters y constructores con sobrecarga según se requiera.
    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Estudiante() {
    }

    public Estudiante(Long matricula, String nombre, @Email String email) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.email = email;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public Estudiante(Long matricula, @NotNull(message = "El nombre no debe ser nulo") String nombre,
            @Email(message = "El email debe ser válido") @NotNull(message = "El email no debe ser nulo") String email,
            List<Inscripcion> inscripciones, List<Solicitud> solicitudes) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.email = email;
        this.inscripciones = inscripciones;
        this.solicitudes = solicitudes;
    }

}