package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaCuadernoAnillado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaCuadernoAnilladoRepository extends JpaRepository<PlantillaCuadernoAnillado, Long> {
    Optional<PlantillaCuadernoAnillado> findByCantidadHojasAndTipoTapaCuadernoAnillado_IdAndMedidaCuadernoAnillado_Id(
            int cantidadHojas, Long tipoTapaCuadernoAnilladoId, Long medidaCuadernoAnilladoId);
}
