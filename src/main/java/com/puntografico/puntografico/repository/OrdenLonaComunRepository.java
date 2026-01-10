package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenLonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenLonaComunRepository extends JpaRepository<OrdenLonaComun, Long> {

    OrdenLonaComun findByOrdenTrabajo_Id(Long id);
}
