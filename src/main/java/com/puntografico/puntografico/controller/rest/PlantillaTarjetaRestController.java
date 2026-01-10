package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaTarjeta;
import com.puntografico.puntografico.repository.PlantillaTarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-tarjeta")
public class PlantillaTarjetaRestController {

    @Autowired
    private PlantillaTarjetaRepository plantillaTarjetaRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoPapelTarjetaId,
            @RequestParam Long tipoColorTarjetaId,
            @RequestParam Long tipoFazTarjetaId,
            @RequestParam Long tipoLaminadoTarjetaId,
            @RequestParam Long medidaTarjetaId,
            @RequestParam Long cantidadTarjetaId
    ) {
        Optional<PlantillaTarjeta> plantilla = plantillaTarjetaRepository
                .findByTipoPapelTarjeta_IdAndTipoColorTarjeta_IdAndTipoFazTarjeta_IdAndTipoLaminadoTarjeta_IdAndMedidaTarjeta_IdAndCantidadTarjeta_Id(
                        tipoPapelTarjetaId, tipoColorTarjetaId, tipoFazTarjetaId, tipoLaminadoTarjetaId, medidaTarjetaId, cantidadTarjetaId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
