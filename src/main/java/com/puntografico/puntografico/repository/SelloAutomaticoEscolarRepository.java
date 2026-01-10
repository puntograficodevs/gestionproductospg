package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.SelloAutomaticoEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelloAutomaticoEscolarRepository extends JpaRepository<SelloAutomaticoEscolar, Long> {
    Optional<SelloAutomaticoEscolar> findByOrdenTrabajo_Id(Long id);
}
