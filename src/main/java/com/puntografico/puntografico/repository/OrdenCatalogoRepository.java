package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCatalogoRepository extends JpaRepository<OrdenCatalogo, Long> {

    OrdenCatalogo findByOrdenTrabajo_Id(Long id);
}
