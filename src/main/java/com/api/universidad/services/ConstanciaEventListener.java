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

    /**
     * Suscriptor orientado a eventos (SOA / EDA).
     * 
     * - @Async: Se ejecuta en un hilo separado para no bloquear la respuesta HTTP del pago.
     * - @TransactionalEventListener(phase = AFTER_COMMIT): Garantiza que el proceso de
     *   generación y envío por correo SOLO se inicie si la transacción de pago se completó
     *   y guardó exitosamente en la base de datos (evita procesar constancias de pagos revertidos).
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alConfirmarsePago(PagoConfirmadoEvent evento){
        //Mensaje auxiliar. Se puede borrar o comentarear si es necesario.
        System.out.println("[SUSCRIPTOR] Recibido PagoConfirmadoEvent para solicitud: " + evento.getIdSolicitud());
        try{
            // 1. Invoca el servicio encargado de generar el archivo/cuerpo de la constancia
            sConstancia.crearConstancia(evento.getIdSolicitud());
            // 2. Invoca el servicio de envío telemático (correo electrónico al estudiante)
            sEnvio.enviaCorreo(evento.getIdSolicitud());
        } catch (Exception e){
            // En caso de fallo en servicios externos o de generación, se registra alerta de auditoría
            sNotificacion.enviarNotificacion("No se pudo generar o enviar la constancia de la solicitud "+evento.getIdSolicitud()+": "+e.getMessage(), "error");
        }
    }
}
