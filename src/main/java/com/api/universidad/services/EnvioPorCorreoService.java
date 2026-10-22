package com.api.universidad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Solicitud;
import com.api.universidad.repositories.ISolicitudRepository;

import jakarta.transaction.Transactional;

@Service
public class EnvioPorCorreoService {

    @Autowired
    ISolicitudRepository rSolicitud;

    @Autowired
    NotificacionService sNotificacion;

    public void validaPrevia(Solicitud solicitud) {
        if (!solicitud.isGenerado()) {
            sNotificacion.enviarNotificacion("La solicitud no ha sido generada previamente", "warn");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La solicitud no ha sido generada previamente.");
        }
    }

    // En un entorno real, no convendría mantener la anotación de transacción al
    // mantener la base de datos abierta durante una operación externa.
    @Transactional
    public Solicitud enviaCorreo(Long id) {
        Solicitud solicitudActual = rSolicitud.findById(id)
                .orElseThrow(() -> {
                    sNotificacion.enviarNotificacion("No se ha encontrado la solicitud", "warn");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la solicitud.");
                });

        validaPrevia(solicitudActual);

        // No validamos duplicidad envío por si es necesario volver a enviar la
        // constancia en PDF.

        /*
         * Aquí se envía el correo con el PDF
         */

        solicitudActual.setEnviado(true);

        Solicitud guardado = rSolicitud.save(solicitudActual);

        sNotificacion.guardarNotificacion(guardado, "info", "La solicitud con ID " + guardado.getId()
                + " ha sido enviada al correo " + guardado.getEstudiante().getEmail() + ".");

        return guardado;

    }

}
