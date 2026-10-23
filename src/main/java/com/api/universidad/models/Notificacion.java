package com.api.universidad.models;

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

@Entity
@Table(name="notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_notificacion", nullable=false)
    private Long id;

    @Column(name="Mensaje", nullable=false)
    private String mensaje;

    @Column(name="fecha", nullable=false, updatable=false)
    private LocalDateTime fecha;

    //Se almacena el ID de la solicitud sobre la cual surge esta notificación.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_solicitud", nullable=true)
    @JsonIgnoreProperties("notificaciones")
    private Solicitud solicitud;

    //Asignar la fecha actual apenas se crea la notificación.
    @PrePersist
    public void onCreate(){
        this.fecha = LocalDateTime.now();
    }

    //Constructores, Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public Notificacion() {
    }

    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
    }

    public Notificacion(Long id, String mensaje, LocalDateTime fecha, Solicitud solicitud) {
        this.id = id;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.solicitud = solicitud;
    }

}
