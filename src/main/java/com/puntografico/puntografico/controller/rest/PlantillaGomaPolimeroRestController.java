package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaGomaPolimero;
import com.puntografico.puntografico.domain.PlantillaSelloMadera;
import com.puntografico.puntografico.repository.PlantillaGomaPolimeroRepository;
import com.puntografico.puntografico.repository.PlantillaSelloMaderaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-goma-polimero")
public class PlantillaGomaPolimeroRestController {

    @Autowired
    private PlantillaGomaPolimeroRepository plantillaGomaPolimeroRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long modeloGomaPolimeroId
    ) {
        Optional<PlantillaGomaPolimero> plantilla =
                plantillaGomaPolimeroRepository
                        .findByModeloGomaPolimero_Id(modeloGomaPolimeroId);

        return plantilla
                .map(p -> ResponseEntity
                        .ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
