package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Vinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViniloRepository extends JpaRepository<Vinilo, Long> {
}
