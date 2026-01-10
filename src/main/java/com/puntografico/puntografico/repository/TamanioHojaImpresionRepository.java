package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TamanioHojaImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanioHojaImpresionRepository extends JpaRepository<TamanioHojaImpresion, Long> {
}
