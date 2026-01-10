package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaSelloAutomaticoEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaSelloAutomaticoEscolarRepository extends JpaRepository<PlantillaSelloAutomaticoEscolar, Long> {
    Optional<PlantillaSelloAutomaticoEscolar> findByModeloSelloAutomaticoEscolar_Id(Long modeloSelloAutomaticoEscolarId);
}
