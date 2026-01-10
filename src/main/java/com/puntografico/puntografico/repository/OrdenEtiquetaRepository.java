package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenEtiquetaRepository extends JpaRepository<OrdenEtiqueta, Long> {

    OrdenEtiqueta findByOrdenTrabajo_Id(Long id);
}
