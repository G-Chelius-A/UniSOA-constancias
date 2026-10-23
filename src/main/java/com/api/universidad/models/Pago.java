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

@Entity
@Table(name="pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_pago", nullable=false)
    private Long id;

    @Column(name="monto", nullable=false, precision=10, scale=2)
    private BigDecimal monto;

    @Column(name="fecha", nullable=false)
    private LocalDateTime fecha;

    @Column(name="tipo_pago", nullable=false)
    private TipoPago tipo;
    public enum TipoPago{
        DEBITO,
        CREDITO,
        EFECTIVO,
        OTRO
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_solicitud", nullable=false)
    @JsonIgnoreProperties("pagos")
    @NotNull(message="El pago debe corresponder a una solicitud")
    private Solicitud solicitud;

    //Asignar la fecha de creación en este preciso momento.

    @PrePersist
    public void onCreate(){
        this.fecha = LocalDateTime.now();
    }

    //Getters, setters y constructores.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public TipoPago getTipo() {
        return tipo;
    }

    public void setTipo(TipoPago tipo) {
        this.tipo = tipo;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public Pago() {
    }

    public Pago(Long id, BigDecimal monto, LocalDateTime fecha, TipoPago tipo,
            @NotNull(message = "El pago debe corresponder a una solicitud") Solicitud solicitud) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.tipo = tipo;
        this.solicitud = solicitud;
    }

}
