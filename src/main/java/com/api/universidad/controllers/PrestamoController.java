package com.api.universidad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.universidad.models.Prestamo;
import com.api.universidad.services.PrestamoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    @Autowired
    private PrestamoService sPrestamo;

    @PostMapping
    public ResponseEntity<Prestamo> registrar(@Valid @RequestBody Prestamo prestamo) {
        Prestamo guardado = sPrestamo.registrarPrestamo(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<Prestamo> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(sPrestamo.devolverLibro(id));
    }
}
