package com.api.universidad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Solicitud;
import com.api.universidad.repositories.ISolicitudRepository;

import jakarta.transaction.Transactional;

@Service
public class GenerarConstanciaService {
    @Autowired
    ISolicitudRepository rSolicitud;

    @Autowired
    NotificacionService sNotificacion;

    public void validaPrevia(Solicitud solicitud) {
        if (!solicitud.isPagado()) {
            sNotificacion.enviarNotificacion("La solicitud no se ha aprobado por falta de pago", "warn");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "La solicitud no se ha aprobado por falta de pago.");
        }
    }

    @Transactional
    public Solicitud crearConstancia(Long id) {
        Solicitud solicitudActual = rSolicitud.findById(id)
                .orElseThrow(() -> {
                    sNotificacion.enviarNotificacion("No se ha encontrado la solicitud", "warn");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la solicitud.");
                });

        validaPrevia(solicitudActual);

        if (solicitudActual.isGenerado()) {
            sNotificacion.enviarNotificacion("La constancia ya ha sido generada", "warn");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La constancia ya ha sido generada");
        }

        /*
         * Aquí se generaría el PDF con una librería o framework externo.
         */

        solicitudActual.setGenerado(true);

        Solicitud guardado = rSolicitud.save(solicitudActual);

        sNotificacion.guardarNotificacion(guardado, "info",
                "La constancia ha sido generada exitosamente. Solicitud con ID: " + guardado.getId());

        return guardado;
    }
}
