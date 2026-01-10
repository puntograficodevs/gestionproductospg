package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenComboRepository extends JpaRepository<OrdenCombo, Long> {

    OrdenCombo findByOrdenTrabajo_Id(Long id);
}
