package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaComboRepository extends JpaRepository<PlantillaCombo, Long> {
    Optional<PlantillaCombo> findByTipoCombo_Id(Long tipoComboId);
}
