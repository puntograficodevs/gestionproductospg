package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenSelloAutomatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenSelloAutomaticoRepository extends JpaRepository<OrdenSelloAutomatico, Long> {

    OrdenSelloAutomatico findByOrdenTrabajo_Id(Long id);
}
