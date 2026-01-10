package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaTalonarioRepository extends JpaRepository<PlantillaTalonario, Long> {

    Optional<PlantillaTalonario> findByCantidadHojasAndConNumeradoAndTipoTalonario_IdAndTipoTroqueladoTalonario_IdAndModoTalonario_IdAndTipoColorTalonario_IdAndMedidaTalonario_IdAndTipoPapelTalonario_IdAndCantidadTalonario_Id(
            int cantidadHojas, boolean conNumerado, Long tipoTalonarioId, Long tipoTroqueladoTalonarioId, Long modoTalonarioId, Long tipoColorTalonarioId, Long medidaTalonarioId, Long tipoPapelTalonarioId, Long cantidadTalonarioId
    );
}
