package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaSublimacion;
import com.puntografico.puntografico.repository.PlantillaSublimacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-sublimacion")
public class PlantillaSublimacionRestController {

    @Autowired
    private PlantillaSublimacionRepository plantillaSublimacionRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long materialSublimacionId,
            @RequestParam Long cantidadSublimacionId
    ) {
        Optional<PlantillaSublimacion> plantilla = plantillaSublimacionRepository
                .findByMaterialSublimacion_IdAndCantidadSublimacion_Id(
                        materialSublimacionId, cantidadSublimacionId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
