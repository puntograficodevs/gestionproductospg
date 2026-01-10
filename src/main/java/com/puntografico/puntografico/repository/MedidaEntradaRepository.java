package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaEntradaRepository extends JpaRepository<MedidaEntrada, Long> {
}
