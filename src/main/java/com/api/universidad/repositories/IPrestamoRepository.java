package com.api.universidad.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Prestamo;

import jakarta.persistence.LockModeType;

@Repository
public interface IPrestamoRepository extends JpaRepository<Prestamo, Long> {
    //Cuando se ejecuta la consulta, se bloqueará toda la fila para evitar lecturas e inserciones no deseadas.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select prestamo from Prestamo prestamo where prestamo.id = :id")
    Optional<Prestamo> findByIdForUpdate(@Param("id") Long id);
}
