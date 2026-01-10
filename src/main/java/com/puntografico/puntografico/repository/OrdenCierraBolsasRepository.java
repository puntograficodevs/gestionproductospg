package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenCierraBolsas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCierraBolsasRepository extends JpaRepository<OrdenCierraBolsas, Long> {

    OrdenCierraBolsas findByOrdenTrabajo_Id(Long id);
}
