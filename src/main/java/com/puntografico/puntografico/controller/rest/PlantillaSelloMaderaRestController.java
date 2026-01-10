package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaSelloMadera;
import com.puntografico.puntografico.repository.PlantillaSelloMaderaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-sello-madera")
public class PlantillaSelloMaderaRestController {

    @Autowired
    private PlantillaSelloMaderaRepository plantillaSelloMaderaRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tamanioSelloMaderaId
    ) {
        Optional<PlantillaSelloMadera> plantilla =
                plantillaSelloMaderaRepository
                        .findByTamanioSelloMadera_Id(tamanioSelloMaderaId);

        return plantilla
                .map(p -> ResponseEntity
                        .ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
