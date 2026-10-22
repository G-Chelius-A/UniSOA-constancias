package com.api.universidad.models;

public class PagoConfirmadoEvent {
    private final Long idSolicitud;

    public PagoConfirmadoEvent(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }
}
