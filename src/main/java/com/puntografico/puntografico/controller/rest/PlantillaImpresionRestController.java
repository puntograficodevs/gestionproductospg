package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaImpresion;
import com.puntografico.puntografico.repository.PlantillaImpresionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-impresion")
public class PlantillaImpresionRestController {

    @Autowired
    private PlantillaImpresionRepository plantillaImpresionRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoColorImpresionId,
            @RequestParam Long tamanioHojaImpresionId,
            @RequestParam Long tipoFazImpresionId,
            @RequestParam Long tipoPapelImpresionId,
            @RequestParam Long cantidadImpresionId,
            @RequestParam Long tipoImpresionId
    ) {
        Optional<PlantillaImpresion> plantilla = plantillaImpresionRepository
                .findByTipoColorImpresion_IdAndTamanioHojaImpresion_IdAndTipoFazImpresion_IdAndTipoPapelImpresion_IdAndCantidadImpresion_IdAndTipoImpresion_Id(
                        tipoColorImpresionId, tamanioHojaImpresionId, tipoFazImpresionId, tipoPapelImpresionId, cantidadImpresionId, tipoImpresionId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
