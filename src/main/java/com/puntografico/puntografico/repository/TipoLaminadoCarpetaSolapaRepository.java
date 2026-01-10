package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoLaminadoCarpetaSolapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLaminadoCarpetaSolapaRepository extends JpaRepository<TipoLaminadoCarpetaSolapa, Long> {
}
