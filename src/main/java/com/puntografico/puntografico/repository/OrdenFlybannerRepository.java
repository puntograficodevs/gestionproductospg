package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenFlybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenFlybannerRepository extends JpaRepository<OrdenFlybanner, Long> {

    OrdenFlybanner findByOrdenTrabajo_Id(Long id);
}
