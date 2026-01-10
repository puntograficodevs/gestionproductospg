package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenSelloAutomaticoEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenSelloAutomaticoEscolarRepository extends JpaRepository<OrdenSelloAutomaticoEscolar, Long> {
    OrdenSelloAutomaticoEscolar findByOrdenTrabajo_Id(Long id);
}
