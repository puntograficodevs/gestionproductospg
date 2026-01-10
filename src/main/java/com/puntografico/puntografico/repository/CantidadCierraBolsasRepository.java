package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadCierraBolsas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadCierraBolsasRepository extends JpaRepository<CantidadCierraBolsas, Long> {
}
