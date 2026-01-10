package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Turnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurneroRepository extends JpaRepository<Turnero, Long> {
    Optional<Turnero> findByOrdenTrabajo_Id(Long id);
}
