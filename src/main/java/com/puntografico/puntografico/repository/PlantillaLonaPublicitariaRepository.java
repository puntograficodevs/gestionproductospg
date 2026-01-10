package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaLonaPublicitaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaLonaPublicitariaRepository extends JpaRepository<PlantillaLonaPublicitaria, Long> {
    Optional<PlantillaLonaPublicitaria> findByConAdicionalPortabannerAndConOjalesAndConOjalesConRefuerzoAndConBolsillosAndConDemasiaParaTensadoAndConSolapadoAndMedidaLonaPublicitaria_IdAndTipoLonaPublicitaria_Id(
            boolean conAdicionalPortabanner, boolean conOjales, boolean conOjalesConRefuerzo, boolean conBolsillos, boolean conDemasiaParaTensado, boolean conSolapado, Long medidaLonaPublicitariaId, Long tipoLonaPublicitariaId
    );
}
