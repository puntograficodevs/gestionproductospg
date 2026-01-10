package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelImpresionRepository extends JpaRepository<TipoPapelImpresion, Long> {
}
