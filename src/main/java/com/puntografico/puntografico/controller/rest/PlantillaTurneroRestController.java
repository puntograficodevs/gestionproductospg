package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaTurnero;
import com.puntografico.puntografico.repository.PlantillaTurneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-turnero")
public class PlantillaTurneroRestController {

    @Autowired
    private PlantillaTurneroRepository plantillaTurneroRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Integer cantidadHojas,
            @RequestParam Long medidaTurneroId,
            @RequestParam Long tipoColorTurneroId,
            @RequestParam Long cantidadTurneroId
    ) {
        if (cantidadHojas != 100) cantidadHojas = null;
        Optional<PlantillaTurnero> plantilla = plantillaTurneroRepository
                .findByCantidadHojasAndTipoColorTurnero_IdAndCantidadTurnero_IdAndMedidaTurnero_Id(
                        cantidadHojas, tipoColorTurneroId, cantidadTurneroId, medidaTurneroId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
