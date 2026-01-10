package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaTarjetaRepository extends JpaRepository<PlantillaTarjeta, Long> {

    Optional<PlantillaTarjeta> findByTipoPapelTarjeta_IdAndTipoColorTarjeta_IdAndTipoFazTarjeta_IdAndTipoLaminadoTarjeta_IdAndMedidaTarjeta_IdAndCantidadTarjeta_Id(
            Long tipoPapelTarjetaId, Long tipoColorTarjetaId, Long tipoFazTarjetaId, Long tipoLaminadoTarjetaId, Long medidaTarjetaId, Long cantidadTarjetaId
    );

}
