package com.api.universidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Inscripcion;
import com.api.universidad.models.Periodo;

import java.util.ArrayList;

@Repository 
public interface IInscripcionRepository extends JpaRepository<Inscripcion, Long> {
    //Existe una inscripción por matrícula de estudiante, periodo y año cursado.
    boolean existsByEstudiante_MatriculaAndPeriodoAndAnio(Long matricula, Periodo periodo, Integer anio);
    //Existe una inscripción por matrícula, ID del curso, periodo y año.
    boolean existsByEstudiante_MatriculaAndCurso_IdAndPeriodoAndAnio(Long matricula, Long idCurso, Periodo periodo, Integer anio);
    ArrayList<Inscripcion> findByEstudiante_Matricula(Long matricula);
    //Con este método del contrato, se podrán obtener las calificaciones siempre y cuando la inscripción esté completa y la calificación no sea nula.
    ArrayList<Inscripcion> findByEstudiante_MatriculaAndCompletadaTrueAndCalificacionIsNotNull(Long matricula);
    ArrayList<Inscripcion> findByCurso_Id(Long idCurso);
}
