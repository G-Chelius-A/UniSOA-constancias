package com.api.universidad.services;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.universidad.models.Notificacion;
import com.api.universidad.models.Solicitud;
import com.api.universidad.repositories.INotificacionRepository;

import jakarta.transaction.Transactional;

@Service 
public class NotificacionService {
    @Autowired 
    INotificacionRepository rNotificacion;

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    public void enviarNotificacion(String mensaje, String tipo){
        switch(tipo.toLowerCase()){
            case "warn":
                logger.warn(mensaje);
                break;
            case "error":
                logger.error(mensaje);
                break;
            case "info":
                logger.info(mensaje);
                break;
            default:
                throw new IllegalArgumentException(
                    "Tipo de notificación inválido"
                );
        }
    }

    @Transactional 
    public Notificacion guardarNotificacion(Notificacion notificacion, String tipo, String mensaje){
        if (notificacion == null) {
            notificacion = new Notificacion();
        }
        notificacion.setMensaje(tipo+": "+mensaje);    
        enviarNotificacion(mensaje, tipo);
        return rNotificacion.save(notificacion);
    }

    @Transactional 
    public Notificacion guardarNotificacion(Solicitud solicitud, String tipo, String mensaje){
        Notificacion notificacion = new Notificacion();
        notificacion.setSolicitud(solicitud);
        return guardarNotificacion(notificacion, tipo, mensaje);
    }

    @Transactional 
    public Notificacion guardarNotificacion(String tipo, String mensaje){
        return guardarNotificacion(new Notificacion(), tipo, mensaje);
    }

    public ArrayList<Notificacion> obtenerNotificaciones(){
        return (ArrayList<Notificacion>) rNotificacion.findAll();
    }
}
