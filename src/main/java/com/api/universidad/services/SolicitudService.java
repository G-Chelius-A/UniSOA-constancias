package com.api.universidad.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.PagoConfirmadoEvent;
import com.api.universidad.models.Solicitud;
import com.api.universidad.repositories.ISolicitudRepository;

import jakarta.transaction.Transactional;

@Service
public class SolicitudService {
    @Autowired
    ISolicitudRepository rSolicitud;

    @Autowired
    VerificaPeriodoService sPeriodo;

    @Autowired
    NotificacionService sNotificacion;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public Solicitud registrarSolicitud(Solicitud solicitud) {

        // 1. Validaciones de presencia obligatoria de campos base
        if (solicitud.getEstudiante() == null) {
            sNotificacion.enviarNotificacion("Debe existir un estudiante en la solicitud.", "warn");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe existir un estudiante");
        }

        if (solicitud.getPeriodo() == null) {
            sNotificacion.enviarNotificacion("Debe existir un periodo en la solicitud.", "warn");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe existir un periodo");
        }

        if (solicitud.getAnio() == null) {
            sNotificacion.enviarNotificacion("Debe existir un año en la solicitud.", "warn");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe existir un año");
        }

        // 2. Regla de negocio SOA: El estudiante no puede pedir constancia de un ciclo
        // en el que no estuvo inscrito a ningún curso
        if (!sPeriodo.verificarPeriodo(solicitud.getEstudiante().getMatricula(), solicitud.getPeriodo(),
                solicitud.getAnio())) {
            sNotificacion.enviarNotificacion(
                    "El estudiante debe tener al menos un curso en el periodo de la solicitud.", "warn");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El estudiante debe tener al menos un curso en el periodo solicitado.");
        }

        // 3. Control de concurrencia e idempotencia: evitar solicitudes duplicadas para el mismo ciclo
        if (rSolicitud.existsByEstudiante_MatriculaAndPeriodoAndAnio(solicitud.getEstudiante().getMatricula(),
                solicitud.getPeriodo(), solicitud.getAnio())) {
            sNotificacion.enviarNotificacion("Ya existe una solicitud para ese periodo", "warn");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una solicitud para ese periodo");
        }

        Solicitud guardado = rSolicitud.save(solicitud);
        sNotificacion.guardarNotificacion(guardado, "info", "Solicitud registrada. ID: " + guardado.getId());
        return guardado;
    }

    @Transactional
    public Solicitud confirmarPagos(Long id) {
        Solicitud solicitudActual = rSolicitud.findById(id)
                .orElseThrow(() -> {
                    sNotificacion.enviarNotificacion("No se ha encontrado una solicitud con el ID " + id, "warn");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No se ha encontrado una solicitud con el ID " + id);
                });

        // Sumar todos los abonos/pagos asociados a esta solicitud en base de datos
        BigDecimal totalPagado = rSolicitud.sumPagosPorSolicitud(id);

        // Validación estricta de saldo: la suma de pagos debe cubrir exactamente el costo total definido
        if (totalPagado == null || totalPagado.compareTo(solicitudActual.getCosto()) != 0) {
            sNotificacion.enviarNotificacion("No se ha cubierto el monto del pago", "warn");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se ha cubierto el monto del pago");
        }

        // Marcar estado pagado
        solicitudActual.setPagado(true);
        Solicitud guardado = rSolicitud.save(solicitudActual);

        sNotificacion.guardarNotificacion(guardado, "info",
                "Pagos confirmados en la solicitud con ID: " + guardado.getId());

        // Event-driven: dispara evento de dominio para procesar asíncronamente
        // la creación del PDF/documento y su posterior envío por correo
        eventPublisher.publishEvent(new PagoConfirmadoEvent(guardado.getId()));

        return guardado;
    }

    public ArrayList<Solicitud> obtenerSolicitudes() {
        return (ArrayList<Solicitud>) rSolicitud.findAll();
    }

    public Optional<Solicitud> obtenerSolicitudId(Long id) {
        return rSolicitud.findById(id);
    }

}