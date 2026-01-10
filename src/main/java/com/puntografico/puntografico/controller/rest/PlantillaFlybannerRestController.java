package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaFlybanner;
import com.puntografico.puntografico.repository.PlantillaFlybannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-flybanner")
public class PlantillaFlybannerRestController {

    @Autowired
    private PlantillaFlybannerRepository plantillaFlybannerRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoFazFlybannerId,
            @RequestParam Long alturaFlybannerId,
            @RequestParam Long banderaFlybannerId,
            @RequestParam Long tipoBaseFlybannerId
    ) {
        Optional<PlantillaFlybanner> plantilla = plantillaFlybannerRepository
                .findByTipoFazFlybanner_IdAndAlturaFlybanner_IdAndBanderaFlybanner_IdAndTipoBaseFlybanner_Id(
                        tipoFazFlybannerId, alturaFlybannerId, banderaFlybannerId, tipoBaseFlybannerId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
