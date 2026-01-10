package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.NumeradoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumeradoEntradaRepository extends JpaRepository<NumeradoEntrada, Long> {
}
