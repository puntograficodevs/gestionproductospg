package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenViniloPlasticoCorrugado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenViniloPlasticoCorrugadoRepository extends JpaRepository<OrdenViniloPlasticoCorrugado, Long> {
    OrdenViniloPlasticoCorrugado findByOrdenTrabajo_Id(Long id);
}
