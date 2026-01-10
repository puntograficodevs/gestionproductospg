package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelFolleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelFolletoRepository extends JpaRepository<TipoPapelFolleto, Long> {
}
