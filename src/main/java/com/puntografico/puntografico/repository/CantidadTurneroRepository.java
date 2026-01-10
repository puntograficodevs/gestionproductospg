package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadTurnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadTurneroRepository extends JpaRepository<CantidadTurnero, Long> {
}
