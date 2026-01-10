package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaSticker;
import com.puntografico.puntografico.repository.PlantillaStickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-sticker")
public class PlantillaStickerRestController {

    @Autowired
    private PlantillaStickerRepository plantillaStickerRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoTroqueladoStickerId,
            @RequestParam Long cantidadStickerId,
            @RequestParam Long medidaStickerId
    ) {
        Optional<PlantillaSticker> plantilla = plantillaStickerRepository
                .findByTipoTroqueladoSticker_IdAndCantidadSticker_IdAndMedidaSticker_Id(
                        tipoTroqueladoStickerId, cantidadStickerId, medidaStickerId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
