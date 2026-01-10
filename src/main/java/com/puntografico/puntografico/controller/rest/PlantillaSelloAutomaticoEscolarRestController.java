package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaSelloAutomaticoEscolar;
import com.puntografico.puntografico.repository.PlantillaSelloAutomaticoEscolarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-sello-automatico-escolar")
public class PlantillaSelloAutomaticoEscolarRestController {

    @Autowired
    private PlantillaSelloAutomaticoEscolarRepository plantillaSelloAutomaticoEscolarRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long modeloSelloAutomaticoEscolarId
    ) {
        Optional<PlantillaSelloAutomaticoEscolar> plantilla =
                plantillaSelloAutomaticoEscolarRepository
                        .findByModeloSelloAutomaticoEscolar_Id(modeloSelloAutomaticoEscolarId);

        return plantilla
                .map(p -> ResponseEntity
                        .ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
