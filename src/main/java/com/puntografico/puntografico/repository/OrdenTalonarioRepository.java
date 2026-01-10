package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenTalonarioRepository extends JpaRepository<OrdenTalonario, Long> {
    OrdenTalonario findByOrdenTrabajo_Id(Long id);
}
