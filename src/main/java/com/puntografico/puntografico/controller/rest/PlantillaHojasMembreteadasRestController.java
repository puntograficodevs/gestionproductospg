package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaHojasMembreteadas;
import com.puntografico.puntografico.repository.PlantillaHojasMembreteadasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-hojas-membreteadas")
public class PlantillaHojasMembreteadasRestController {

    @Autowired
    private PlantillaHojasMembreteadasRepository plantillaHojasMembreteadasRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam int cantidadHojas,
            @RequestParam Long medidaHojasMembreteadasId,
            @RequestParam Long tipoColorHojasMembreteadasId,
            @RequestParam Long cantidadHojasMembreteadasId
    ) {
        Optional<PlantillaHojasMembreteadas> plantilla = plantillaHojasMembreteadasRepository
                .findByCantidadHojasAndMedidaHojasMembreteadas_IdAndTipoColorHojasMembreteadas_IdAndCantidadHojasMembreteadas_Id(
                        cantidadHojas, medidaHojasMembreteadasId, tipoColorHojasMembreteadasId, cantidadHojasMembreteadasId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
