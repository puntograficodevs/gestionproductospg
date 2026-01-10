package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenHojasMembreteadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenHojasMembreteadasRepository extends JpaRepository<OrdenHojasMembreteadas, Long> {

    OrdenHojasMembreteadas findByOrdenTrabajo_Id(Long id);
}
