package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenViniloDeCorte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenViniloDeCorteRepository extends JpaRepository<OrdenViniloDeCorte, Long> {
    OrdenViniloDeCorte findByOrdenTrabajo_Id(Long id);
}
