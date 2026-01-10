package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaSobre;
import com.puntografico.puntografico.repository.PlantillaSobreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-sobre")
public class PlantillaSobreRestController {

    @Autowired
    private PlantillaSobreRepository plantillaSobreRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long medidaSobreId,
            @RequestParam Long tipoColorSobreId,
            @RequestParam Long cantidadSobreId
    ) {
        Optional<PlantillaSobre> plantilla = plantillaSobreRepository
                .findByMedidaSobre_IdAndTipoColorSobre_IdAndCantidadSobre_Id(
                        medidaSobreId, tipoColorSobreId, cantidadSobreId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
