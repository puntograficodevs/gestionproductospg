package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.SelloAutomatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelloAutomaticoRepository extends JpaRepository<SelloAutomatico, Long> {
    Optional<SelloAutomatico> findByOrdenTrabajo_Id(Long id);
}
