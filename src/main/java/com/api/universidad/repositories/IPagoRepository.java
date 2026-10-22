package com.api.universidad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.universidad.models.Pago;

@Repository 
public interface IPagoRepository extends JpaRepository<Pago, Long> {
    
}
