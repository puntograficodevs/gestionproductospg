package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaEntradaRepository extends JpaRepository<PlantillaEntrada, Long> {
    Optional<PlantillaEntrada> findByTipoPapelEntrada_IdAndTipoColorEntrada_IdAndTipoTroqueladoEntrada_IdAndMedidaEntrada_IdAndCantidadEntrada_IdAndNumeradoEntrada_IdAndTerminacionEntrada_Id(
            Long tipoPapelEntradaId, Long tipoColorEntradaId, Long tipoTroqueladoEntradaId, Long medidaEntradaId, Long cantidadEntradaId, Long numeradoEntradaId, Long terminacionEntradaId);
}
