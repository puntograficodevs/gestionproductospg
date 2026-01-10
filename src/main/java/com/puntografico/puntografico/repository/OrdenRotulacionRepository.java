package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenRotulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRotulacionRepository extends JpaRepository<OrdenRotulacion, Long> {
    OrdenRotulacion findByOrdenTrabajo_Id(Long id);
}
