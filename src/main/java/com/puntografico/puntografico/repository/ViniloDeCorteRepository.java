package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.ViniloDeCorte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViniloDeCorteRepository extends JpaRepository<ViniloDeCorte, Long> {
}
