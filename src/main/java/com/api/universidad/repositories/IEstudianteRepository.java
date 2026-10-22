package com.api.universidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Estudiante;

import java.util.Optional;

@Repository 
public interface IEstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByEmail(String email);
    boolean existsByEmail(String email);
}
