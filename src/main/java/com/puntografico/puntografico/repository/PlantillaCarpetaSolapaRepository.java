package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaCarpetaSolapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaCarpetaSolapaRepository extends JpaRepository<PlantillaCarpetaSolapa, Long> {
    Optional<PlantillaCarpetaSolapa> findByTipoLaminadoCarpetaSolapa_IdAndTipoFazCarpetaSolapa_IdAndCantidadCarpetaSolapa_Id(
            Long tipoLaminadoCarpetaSolapaId, Long tipoFazCarpetaSolapaId, Long cantidadCarpetaSolapaId);
}
