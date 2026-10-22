package com.api.universidad.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity 
@Table(name="solicitudes")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_solicitud", nullable=false)
    private Long id;

    @Column(name="costo", precision=10, scale=2, nullable=false)
    private BigDecimal costo;

    @Column(name="fecha", nullable=false)
    private LocalDateTime fecha;

    @Column(name="mensaje_adicional", length=255)
    private String mensaje;

    @Column(name="anio_solicitado", nullable=false)
    @NotNull(message="Es necesario ingresar un año")
    private Integer anio;

    @Column(name="periodo_solicitado", length=255, nullable=false)
    @NotNull(message="Es necesario ingresar un periodo (ENE_JUN / AGO_DIC)")
    private Periodo periodo;

    @Column(name="pagado", nullable=false)
    private boolean pagado;

    @Column(name="enviado", nullable=false)
    private boolean enviado;

    @Column(name="generado", nullable=false)
    private boolean generado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_estudiante", nullable=false)
    @JsonIgnoreProperties("solicitudes")
    @NotNull(message="Debe indicarse un estudiante")
    private Estudiante estudiante;

    @JsonIgnore
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JsonIgnoreProperties("solicitud")
    private List<Pago> pagos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JsonIgnoreProperties("solicitud")
    private List<Notificacion> Notificaciones = new ArrayList<>();

    @PrePersist
    public void onCreate(){
        this.fecha = LocalDateTime.now();
        this.pagado = false;
        this.enviado = false;
        this.generado = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<Notificacion> getNotificaciones() {
        return Notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        Notificaciones = notificaciones;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }    

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }
    
    public boolean isGenerado() {
        return generado;
    }

    public void setGenerado(boolean generado) {
        this.generado = generado;
    }

    public Solicitud() {
    }

    public Solicitud(Long id, BigDecimal costo, LocalDateTime fecha, String mensaje,
            @NotNull(message = "Es necesario ingresar un año") Integer anio,
            @NotNull(message = "Es necesario ingresar un periodo (ENE_JUN / AGO_DIC)") Periodo periodo,
            @NotNull(message = "Debe indicarse un estudiante") Estudiante estudiante, List<Pago> pagos,
            List<Notificacion> notificaciones) {
        this.id = id;
        this.costo = costo;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.anio = anio;
        this.periodo = periodo;
        this.estudiante = estudiante;
        this.pagos = pagos;
        Notificaciones = notificaciones;
    }

    public Solicitud(Long id, BigDecimal costo, LocalDateTime fecha, String mensaje,
            @NotNull(message = "Es necesario ingresar un año") Integer anio,
            @NotNull(message = "Es necesario ingresar un periodo (ENE_JUN / AGO_DIC)") Periodo periodo, boolean pagado,
            boolean enviado, boolean generado,
            @NotNull(message = "Debe indicarse un estudiante") Estudiante estudiante, List<Pago> pagos,
            List<Notificacion> notificaciones) {
        this.id = id;
        this.costo = costo;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.anio = anio;
        this.periodo = periodo;
        this.pagado = pagado;
        this.enviado = enviado;
        this.generado = generado;
        this.estudiante = estudiante;
        this.pagos = pagos;
        Notificaciones = notificaciones;
    }

}
