package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadEtiquetaRepository extends JpaRepository<CantidadEtiqueta, Long> {
}
