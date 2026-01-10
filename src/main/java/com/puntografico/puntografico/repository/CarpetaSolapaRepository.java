package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CarpetaSolapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarpetaSolapaRepository extends JpaRepository<CarpetaSolapa, Long> {

}