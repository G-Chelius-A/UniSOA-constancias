package com.api.universidad.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.universidad.models.Notificacion;
import com.api.universidad.services.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    NotificacionService sNotificacion;

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodas() {
        return ResponseEntity.ok(
            sNotificacion.obtenerNotificaciones()
        );
    }
}
