package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenSublimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenSublimacionRepository extends JpaRepository<OrdenSublimacion, Long> {
    OrdenSublimacion findByOrdenTrabajo_Id(Long id);
}
