package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaVinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaViniloRepository extends JpaRepository<MedidaVinilo, Long> {
}
