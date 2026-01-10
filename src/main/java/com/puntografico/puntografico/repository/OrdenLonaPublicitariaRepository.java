package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenLonaPublicitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenLonaPublicitariaRepository extends JpaRepository<OrdenLonaPublicitaria, Long> {

    OrdenLonaPublicitaria findByOrdenTrabajo_Id(Long id);
}
