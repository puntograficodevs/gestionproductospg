package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenTarjetaRepository extends JpaRepository<OrdenTarjeta, Long> {
    OrdenTarjeta findByOrdenTrabajo_Id(Long id);
}
