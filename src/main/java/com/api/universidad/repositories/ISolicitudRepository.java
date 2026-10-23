package com.api.universidad.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Periodo;
import com.api.universidad.models.Solicitud;


@Repository
public interface ISolicitudRepository
        extends JpaRepository<Solicitud, Long> {

    //El estudiante que solicita la constancia tuvo al menos un curso en el periodo y año que indicó.
    boolean existsByEstudiante_MatriculaAndPeriodoAndAnio(Long matricula, Periodo periodo, Integer anio);

    //Suma todos los montos pagados pora una solicitud.
    @Query("SELECT SUM(p.monto) FROM Pago p " + "WHERE p.solicitud.id = :idSolicitud")
    BigDecimal sumPagosPorSolicitud(@Param("idSolicitud") Long idSolicitud);
}

