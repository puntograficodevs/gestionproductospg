package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaSelloAutomatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaSelloAutomaticoRepository extends JpaRepository<PlantillaSelloAutomatico, Long> {
    Optional<PlantillaSelloAutomatico> findByModeloSelloAutomatico_Id(Long modeloSelloAutomaticoId);
}
