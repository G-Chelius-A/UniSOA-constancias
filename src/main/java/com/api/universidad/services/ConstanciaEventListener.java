package com.api.universidad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.api.universidad.models.PagoConfirmadoEvent;

@Component 
public class ConstanciaEventListener {
    @Autowired 
    GenerarConstanciaService sConstancia;

    @Autowired
    EnvioPorCorreoService sEnvio;

    @Autowired 
    NotificacionService sNotificacion;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alConfirmarsePago(PagoConfirmadoEvent evento){
        System.out.println("[SUSCRIPTOR] Recibido PagoConfirmadoEvent para solicitud");
        try{
            sConstancia.crearConstancia(evento.getIdSolicitud());
            sEnvio.enviaCorreo(evento.getIdSolicitud());
        } catch (Exception e){
            sNotificacion.enviarNotificacion("No se pudo generar o enviar la constancia de la solicitud "+evento.getIdSolicitud()+": "+e.getMessage(), "error");
        }
    }
}
