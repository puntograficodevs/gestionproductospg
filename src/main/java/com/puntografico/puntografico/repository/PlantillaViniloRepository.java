package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaVinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaViniloRepository extends JpaRepository<PlantillaVinilo, Long> {

    Optional<PlantillaVinilo> findByTipoAdicionalVinilo_IdAndTipoCorteVinilo_IdAndMedidaVinilo_IdAndCantidadVinilo_Id(
            Long tipoAdicionalViniloId, Long tipoCorteViniloId, Long medidaViniloId, Long cantidadViniloId
    );
}
