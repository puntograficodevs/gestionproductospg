package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenImpresionRepository extends JpaRepository<OrdenImpresion, Long> {
    OrdenImpresion findByOrdenTrabajo_Id(Long id);
}
