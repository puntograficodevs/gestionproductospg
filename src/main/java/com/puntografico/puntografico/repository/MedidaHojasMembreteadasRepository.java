package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaHojasMembreteadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaHojasMembreteadasRepository extends JpaRepository<MedidaHojasMembreteadas, Long> {
}
