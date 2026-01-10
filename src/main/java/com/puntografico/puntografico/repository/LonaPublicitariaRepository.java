package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.LonaPublicitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LonaPublicitariaRepository extends JpaRepository<LonaPublicitaria, Long> {
    Optional<LonaPublicitaria> findByOrdenTrabajo_Id(Long id);
}
