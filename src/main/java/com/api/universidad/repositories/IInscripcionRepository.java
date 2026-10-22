package com.api.universidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Inscripcion;
import com.api.universidad.models.Periodo;

import java.util.ArrayList;

@Repository 
public interface IInscripcionRepository extends JpaRepository<Inscripcion, Long> {
    boolean existsByEstudiante_MatriculaAndPeriodoAndAnio(Long matricula, Periodo periodo, Integer anio);
    boolean existsByEstudiante_MatriculaAndCurso_IdAndPeriodoAndAnio(Long matricula, Long idCurso, Periodo periodo, Integer anio);
    ArrayList<Inscripcion> findByEstudiante_Matricula(Long matricula);
    ArrayList<Inscripcion> findByCurso_Id(Long idCurso);
}
