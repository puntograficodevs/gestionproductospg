package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaAgendaRepository extends JpaRepository<PlantillaAgenda, Long> {
    Optional<PlantillaAgenda> findByCantidadHojasAndTipoTapaAgenda_IdAndTipoColorAgenda_Id(
            int cantidadHojas, Long tipoTapaId, Long tipoColorId);
}
