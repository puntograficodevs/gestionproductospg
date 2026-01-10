package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorEntradaRepository extends JpaRepository<TipoColorEntrada, Long> {
}
