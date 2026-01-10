package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorTurnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorTurneroRepository extends JpaRepository<TipoColorTurnero, Long> {
}
