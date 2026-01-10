package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Optional<Agenda> findByOrdenTrabajo_Id(Long id);
}
