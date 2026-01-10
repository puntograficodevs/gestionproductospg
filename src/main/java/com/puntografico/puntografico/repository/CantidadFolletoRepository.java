package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadFolleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadFolletoRepository extends JpaRepository<CantidadFolleto, Long> {
}
