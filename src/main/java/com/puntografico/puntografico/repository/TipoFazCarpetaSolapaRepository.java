package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazCarpetaSolapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazCarpetaSolapaRepository extends JpaRepository<TipoFazCarpetaSolapa, Long> {
}
