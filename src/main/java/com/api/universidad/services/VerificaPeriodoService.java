package com.api.universidad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.universidad.models.Periodo;
import com.api.universidad.repositories.IInscripcionRepository;

/*
    Servicio dedicado única y exclusivamente para validar y verificar la existencia de una inscripción por matrícula, periodo y año.
*/

@Service 
public class VerificaPeriodoService {
    @Autowired 
    IInscripcionRepository rInscripcion;

    public boolean verificarPeriodo(Long matricula, Periodo periodo, Integer anio){

            return rInscripcion.existsByEstudiante_MatriculaAndPeriodoAndAnio(matricula, periodo, anio);

    }
}
