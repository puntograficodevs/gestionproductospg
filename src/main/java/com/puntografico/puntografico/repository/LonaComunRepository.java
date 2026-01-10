package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.LonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LonaComunRepository extends JpaRepository<LonaComun, Long> {
    Optional<LonaComun> findByOrdenTrabajo_Id(Long id);
}
