package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorAgendaRepository extends JpaRepository<TipoColorAgenda, Long> {
}
