package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenSobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenSobreRepository extends JpaRepository<OrdenSobre, Long> {

    OrdenSobre findByOrdenTrabajo_Id(Long id);
}
