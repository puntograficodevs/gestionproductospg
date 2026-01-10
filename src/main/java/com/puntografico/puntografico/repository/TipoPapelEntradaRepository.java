package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelEntradaRepository extends JpaRepository<TipoPapelEntrada, Long> {
}
