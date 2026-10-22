package com.api.universidad.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.universidad.models.Solicitud;
import com.api.universidad.services.SolicitudService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    SolicitudService sSolicitud;

    @GetMapping
    public ResponseEntity<ArrayList<Solicitud>> obtenerTodas() {
        return ResponseEntity.ok(sSolicitud.obtenerSolicitudes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {

        Solicitud solicitud = sSolicitud.obtenerSolicitudId(id)
            .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND,"No se ha encontrado la solicitud con ID: " + id));

        return ResponseEntity.ok(solicitud);
    }

    @PostMapping
    public ResponseEntity<Solicitud> guardar(@Valid @RequestBody Solicitud solicitud) {

        Solicitud guardada = sSolicitud.registrarSolicitud(solicitud);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PostMapping("/{id}/confirmar-pagos")
    public ResponseEntity<Solicitud> confirmarPagos(@PathVariable Long id) {
        Solicitud solicitud = sSolicitud.confirmarPagos(id);
        return ResponseEntity.ok(solicitud);
    }
}

