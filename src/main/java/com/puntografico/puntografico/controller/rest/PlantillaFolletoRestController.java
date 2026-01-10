package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaFolleto;
import com.puntografico.puntografico.repository.PlantillaFolletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-folleto")
public class PlantillaFolletoRestController {

    @Autowired
    private PlantillaFolletoRepository plantillaFolletoRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoPapelFolletoId,
            @RequestParam Long tipoColorFolletoId,
            @RequestParam Long tipoFazFolletoId,
            @RequestParam Long tamanioHojaFolletoId,
            @RequestParam Long tipoFolletoId,
            @RequestParam Long cantidadFolletoId
    ) {
        Optional<PlantillaFolleto> plantilla = plantillaFolletoRepository
                .findByTipoPapelFolleto_IdAndTipoColorFolleto_IdAndTipoFazFolleto_IdAndTamanioHojaFolleto_IdAndTipoFolleto_IdAndCantidadFolleto_Id(
                tipoPapelFolletoId, tipoColorFolletoId, tipoFazFolletoId, tamanioHojaFolletoId, tipoFolletoId, cantidadFolletoId
        );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
