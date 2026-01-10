package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaLonaPublicitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaLonaPublicitariaRepository extends JpaRepository<MedidaLonaPublicitaria, Long> {
}
