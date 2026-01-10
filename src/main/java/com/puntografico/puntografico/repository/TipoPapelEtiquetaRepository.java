package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelEtiquetaRepository extends JpaRepository<TipoPapelEtiqueta, Long> {
}
