package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelTarjetaRepository extends JpaRepository<TipoPapelTarjeta, Long> {
}
