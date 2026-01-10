package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaSelloAutomatico;
import com.puntografico.puntografico.repository.PlantillaSelloAutomaticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-sello-automatico")
public class PlantillaSelloAutomaticoRestController {

    @Autowired
    private PlantillaSelloAutomaticoRepository plantillaSelloAutomaticoRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long modeloSelloAutomaticoId
    ) {
        Optional<PlantillaSelloAutomatico> plantilla =
                plantillaSelloAutomaticoRepository
                        .findByModeloSelloAutomatico_Id(modeloSelloAutomaticoId);

        return plantilla
                .map(p -> ResponseEntity
                        .ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
