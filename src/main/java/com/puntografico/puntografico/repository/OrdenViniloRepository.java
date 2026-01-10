package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenVinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenViniloRepository extends JpaRepository<OrdenVinilo, Long> {
    OrdenVinilo findByOrdenTrabajo_Id(Long id);
}
