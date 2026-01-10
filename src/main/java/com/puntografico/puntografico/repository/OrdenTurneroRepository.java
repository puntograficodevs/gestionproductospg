package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenTurnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenTurneroRepository extends JpaRepository<OrdenTurnero, Long> {
    OrdenTurnero findByOrdenTrabajo_Id(Long id);
}
