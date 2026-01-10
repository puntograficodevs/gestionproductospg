package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenRifasBonosContribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRifasBonosContribucionRepository extends JpaRepository<OrdenRifasBonosContribucion, Long> {
    OrdenRifasBonosContribucion findByOrdenTrabajo_Id(Long id);
}
