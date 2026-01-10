package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenCarpetaSolapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCarpetaSolapaRepository extends JpaRepository<OrdenCarpetaSolapa, Long> {
    OrdenCarpetaSolapa findByOrdenTrabajo_Id(Long id);
}
