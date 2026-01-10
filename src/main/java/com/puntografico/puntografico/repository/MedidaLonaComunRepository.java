package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaLonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaLonaComunRepository extends JpaRepository<MedidaLonaComun, Long> {
}
