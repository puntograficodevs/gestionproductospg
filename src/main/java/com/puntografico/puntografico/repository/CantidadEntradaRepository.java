package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadEntradaRepository extends JpaRepository<CantidadEntrada, Long> {
}
