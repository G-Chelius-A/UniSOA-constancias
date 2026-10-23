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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select libro from Libro libro where libro.id = :id")
    Optional<Libro> findByIdForUpdate(@Param("id") Long id);
}
