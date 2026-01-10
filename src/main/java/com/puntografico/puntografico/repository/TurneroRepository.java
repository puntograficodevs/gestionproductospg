package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Turnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurneroRepository extends JpaRepository<Turnero, Long> {
}
