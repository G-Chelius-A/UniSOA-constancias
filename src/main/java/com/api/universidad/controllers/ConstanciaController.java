package com.api.universidad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.universidad.models.Solicitud;
import com.api.universidad.services.EnvioPorCorreoService;
import com.api.universidad.services.GenerarConstanciaService;

@RestController
@RequestMapping("/api/constancias")
public class ConstanciaController {

    @Autowired
    GenerarConstanciaService sConstancia;

    @Autowired
    EnvioPorCorreoService sEnvio;

    @PostMapping("/{id}/generar")
    public ResponseEntity<Solicitud> generar(@PathVariable Long id) {

        Solicitud solicitud = sConstancia.crearConstancia(id);

        return ResponseEntity.ok(solicitud);
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<Solicitud> enviar(@PathVariable Long id) {

        Solicitud solicitud = sEnvio.enviaCorreo(id);

        return ResponseEntity.ok(solicitud);
    }
}
