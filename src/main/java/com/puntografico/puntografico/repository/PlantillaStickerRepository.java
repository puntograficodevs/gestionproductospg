package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaStickerRepository extends JpaRepository<PlantillaSticker, Long> {

    Optional<PlantillaSticker> findByTipoTroqueladoSticker_IdAndCantidadSticker_IdAndMedidaSticker_Id(
            Long tipoTroqueladoStickerId, Long cantidadStickerId, Long medidaStickerId
    );
}
