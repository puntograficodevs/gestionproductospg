package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenEntradaRepository extends JpaRepository<OrdenEntrada, Long> {

    OrdenEntrada findByOrdenTrabajo_Id(Long id);
}
