package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadImpresionRepository extends JpaRepository<CantidadImpresion, Long> {
}
