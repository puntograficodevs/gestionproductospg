package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaEtiquetaRepository extends JpaRepository<MedidaEtiqueta, Long> {
}
