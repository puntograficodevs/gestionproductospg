package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFolleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFolletoRepository extends JpaRepository<TipoFolleto, Long> {
}
