package com.api.universidad.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Notificacion;
import com.api.universidad.models.Pago;
import com.api.universidad.models.PagoConfirmadoEvent;
import com.api.universidad.models.Solicitud;
import com.api.universidad.repositories.IPagoRepository;
import com.api.universidad.repositories.ISolicitudRepository;

import jakarta.transaction.Transactional;

@Service
public class PagoService {
    @Autowired
    IPagoRepository rPago;

    @Autowired
    ISolicitudRepository rSolicitud;

    @Autowired
    SolicitudService sSolicitud;

    @Autowired
    NotificacionService sNotificacion;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public Pago registrarPago(Pago pago) {

        if (pago.getSolicitud() == null ||
                pago.getSolicitud().getId() == null) {

            sNotificacion.enviarNotificacion(
                    "Intento de registrar pago sin solicitud válida",
                    "warn");

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Se debe indicar una solicitud válida");
        }

        if (pago.getMonto() == null ||
                pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {

            sNotificacion.enviarNotificacion(
                    "El monto del pago debe ser positivo",
                    "warn");

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El monto debe ser mayor que cero");
        }

        if (pago.getTipo() == null ||
                !esValido(pago.getTipo().toString())) {

            sNotificacion.enviarNotificacion(
                    "Se debe indicar un tipo de pago existente",
                    "warn");

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Se debe indicar un tipo de pago existente");
        }

        // Recuperar la solicitud persistida
        Long idSolicitud = pago.getSolicitud().getId();

        Solicitud solicitud = rSolicitud.findById(idSolicitud)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se ha encontrado la solicitud con ID: " + idSolicitud));

        // Verificar que tenga un costo definido
        if (solicitud.getCosto() == null) {
            sNotificacion.enviarNotificacion(
                    "La solicitud no tiene un costo definido",
                    "error");

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "La solicitud no tiene un costo definido");
        }

        // Validar que el pago no supere el costo total
        if (pago.getMonto().compareTo(solicitud.getCosto()) > 0) {
            sNotificacion.enviarNotificacion(
                    "El monto del pago supera el costo de la solicitud",
                    "warn");

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El monto del pago no debe superar el costo de la solicitud");
        }

        if (solicitud.isPagado()) {
            sNotificacion.enviarNotificacion("La solicitud ya está pagada", "warn");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La solicitud ya está pagada");
        }

        // Asociar la entidad completa recuperada de la base de datos
        pago.setSolicitud(solicitud);

        // Asignar la fecha si no se recibió
        if (pago.getFecha() == null) {
            pago.setFecha(LocalDateTime.now());
        }

        Pago guardado = rPago.save(pago);

        // Comprobar si el total pagado cubre el costo
        BigDecimal totalPagado = rSolicitud.sumPagosPorSolicitud(idSolicitud);

        if (totalPagado != null &&
                totalPagado.compareTo(solicitud.getCosto()) == 0) {

            solicitud.setPagado(true);
            rSolicitud.save(solicitud);

            eventPublisher.publishEvent(
                    new PagoConfirmadoEvent(idSolicitud));

            sNotificacion.guardarNotificacion(
                    new Notificacion(),
                    "info",
                    "Pago completado para la solicitud con ID: "
                            + idSolicitud);
        }

        sNotificacion.guardarNotificacion(
                new Notificacion(),
                "info",
                "Pago registrado. ID: " + guardado.getId());

        return guardado;
    }

    // Lo ideal es no actualizar ni eliminar pagos, por cuestiones de seguridad y
    // transparencia en la información

    public ArrayList<Pago> obtenerPagos() {
        return (ArrayList<Pago>) rPago.findAll();
    }

    public Optional<Pago> obtenerPagoId(Long idPago) {
        return rPago.findById(idPago);
    }

    public static boolean esValido(String nombre) {
        if (nombre == null)
            return false;
        for (Pago.TipoPago t : Pago.TipoPago.values()) {
            if (t.name().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }
}
