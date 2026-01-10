package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TerminacionEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminacionEntradaRepository extends JpaRepository<TerminacionEntrada, Long> {
}
