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

import com.api.universidad.models.Pago;
import com.api.universidad.services.PagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    @Autowired 
    PagoService sPago;

    @GetMapping
    public ResponseEntity<ArrayList<Pago>> obtenerTodas() {
        return ResponseEntity.ok(sPago.obtenerPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {

        Pago pago = sPago.obtenerPagoId(id)
            .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND,"No se ha encontrado la solicitud con ID: " + id));

        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<Pago> guardar(@Valid @RequestBody Pago pago) {

        Pago guardada = sPago.registrarPago(pago);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
}
