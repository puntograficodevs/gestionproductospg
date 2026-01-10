package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaFlybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaFlybannerRepository extends JpaRepository<PlantillaFlybanner, Long> {

    Optional<PlantillaFlybanner> findByTipoFazFlybanner_IdAndAlturaFlybanner_IdAndBanderaFlybanner_IdAndTipoBaseFlybanner_Id(
            Long tipoFazFlybannerId, Long alturaFlybannerId, Long banderaFlybannerId, Long tipoBaseFlybannerId
    );
}
