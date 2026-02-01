package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.GomaPolimero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GomaPolimeroRepository extends JpaRepository<GomaPolimero, Long> {
    Optional<GomaPolimero> findByOrdenTrabajo_Id(Long id);
}
