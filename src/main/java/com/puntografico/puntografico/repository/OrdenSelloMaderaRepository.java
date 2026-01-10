package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenSelloMadera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenSelloMaderaRepository extends JpaRepository<OrdenSelloMadera, Long> {

    OrdenSelloMadera findByOrdenTrabajo_Id(Long id);
}
