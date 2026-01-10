package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoTroqueladoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTroqueladoEntradaRepository extends JpaRepository<TipoTroqueladoEntrada, Long> {
}
