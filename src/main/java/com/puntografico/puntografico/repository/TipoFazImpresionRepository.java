package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazImpresionRepository extends JpaRepository<TipoFazImpresion, Long> {
}
