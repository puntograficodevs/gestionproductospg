package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaTurnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaTurneroRepository extends JpaRepository<PlantillaTurnero, Long> {

    Optional<PlantillaTurnero> findByCantidadHojasAndTipoColorTurnero_IdAndCantidadTurnero_IdAndMedidaTurnero_Id(
            Integer cantidadHojas, Long tipoColorTurneroId, Long cantidadTurneroId, Long medidaTurneroId
    );
}
