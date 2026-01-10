package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaVinilo;
import com.puntografico.puntografico.repository.PlantillaViniloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-vinilo")
public class PlantillaViniloRestController {

    @Autowired
    private PlantillaViniloRepository plantillaViniloRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoAdicionalViniloId,
            @RequestParam Long tipoCorteViniloId,
            @RequestParam Long medidaViniloId,
            @RequestParam Long cantidadViniloId
    ) {
        Optional<PlantillaVinilo> plantilla = plantillaViniloRepository
                .findByTipoAdicionalVinilo_IdAndTipoCorteVinilo_IdAndMedidaVinilo_IdAndCantidadVinilo_Id(
                        tipoAdicionalViniloId, tipoCorteViniloId, medidaViniloId, cantidadViniloId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
