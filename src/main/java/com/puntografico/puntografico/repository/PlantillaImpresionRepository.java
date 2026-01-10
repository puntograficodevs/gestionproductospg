package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaImpresionRepository extends JpaRepository<PlantillaImpresion, Long> {
    Optional<PlantillaImpresion> findByTipoColorImpresion_IdAndTamanioHojaImpresion_IdAndTipoFazImpresion_IdAndTipoPapelImpresion_IdAndCantidadImpresion_IdAndTipoImpresion_Id(
            Long tipoColorImpresionId, Long tamanioHojaImpresionId, Long tipoFazImpresionId, Long tipoPapelImpresionId, Long cantidadImpresionId, Long tipoImpresionId
    );
}
