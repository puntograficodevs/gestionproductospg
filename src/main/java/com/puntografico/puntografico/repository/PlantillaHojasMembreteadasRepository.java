package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaHojasMembreteadas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaHojasMembreteadasRepository extends JpaRepository<PlantillaHojasMembreteadas, Long> {
    Optional<PlantillaHojasMembreteadas> findByCantidadHojasAndMedidaHojasMembreteadas_IdAndTipoColorHojasMembreteadas_IdAndCantidadHojasMembreteadas_Id(
            int cantidadHojas, Long medidaHojasMembreteadasId, Long tipoColorHojasMembreteadasId, Long cantidadHojasMembreteadasid
    );
}
