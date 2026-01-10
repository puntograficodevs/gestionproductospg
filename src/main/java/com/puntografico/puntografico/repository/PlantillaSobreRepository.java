package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaSobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaSobreRepository extends JpaRepository<PlantillaSobre, Long> {

    Optional<PlantillaSobre> findByMedidaSobre_IdAndTipoColorSobre_IdAndCantidadSobre_Id(
            Long medidaSobreId, Long tipoColorSobreId, Long cantidadSobreId
    );
}
