package com.api.universidad.models;


//Clase que se instancia al momento en que se dispare un Evento.
public class PagoConfirmadoEvent {
    private final Long idSolicitud;

    public PagoConfirmadoEvent(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }
}
