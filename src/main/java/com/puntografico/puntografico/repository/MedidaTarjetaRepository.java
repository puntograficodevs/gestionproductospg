package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaTarjetaRepository extends JpaRepository<MedidaTarjeta, Long> {
}
