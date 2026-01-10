package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenOtro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenOtroRepository extends JpaRepository<OrdenOtro, Long> {

    OrdenOtro findByOrdenTrabajo_Id(Long id);
}
