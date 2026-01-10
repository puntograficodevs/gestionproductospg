package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorFolleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorFolletoRepository extends JpaRepository<TipoColorFolleto, Long> {
}
