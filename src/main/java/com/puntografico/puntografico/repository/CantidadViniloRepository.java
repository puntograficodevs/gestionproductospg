package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadVinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadViniloRepository extends JpaRepository<CantidadVinilo, Long> {
}
