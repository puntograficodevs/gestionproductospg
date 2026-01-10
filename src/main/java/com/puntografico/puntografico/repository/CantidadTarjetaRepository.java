package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadTarjetaRepository extends JpaRepository<CantidadTarjeta, Long> {
}
