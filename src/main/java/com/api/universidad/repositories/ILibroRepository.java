package com.api.universidad.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Libro;

import jakarta.persistence.LockModeType;

@Repository
public interface ILibroRepository extends JpaRepository<Libro, Long> {
    //Cuando realiza la consulta, se realizará un bloqueo en toda fila de ese libro para asegurar que ninguna otra transacción lea o modifique el registro.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select libro from Libro libro where libro.id = :id")
    Optional<Libro> findByIdForUpdate(@Param("id") Long id);

    boolean existsByIsbn(String isbn);

    Optional<Libro> findByIsbn(String isbn);
}

