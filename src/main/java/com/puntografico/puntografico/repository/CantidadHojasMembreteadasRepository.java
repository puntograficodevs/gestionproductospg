package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadHojasMembreteadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadHojasMembreteadasRepository extends JpaRepository<CantidadHojasMembreteadas, Long> {
}
