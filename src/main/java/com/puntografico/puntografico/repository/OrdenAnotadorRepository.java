package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenAnotador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenAnotadorRepository extends JpaRepository<OrdenAnotador, Long> {

    OrdenAnotador findByOrdenTrabajo_Id(Long id);
}
