package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaSublimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaSublimacionRepository extends JpaRepository<PlantillaSublimacion, Long> {

    Optional<PlantillaSublimacion> findByMaterialSublimacion_IdAndCantidadSublimacion_Id(Long materialSublimacionId, Long cantidadSublimacionId);
}
