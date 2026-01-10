package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaCierraBolsas;
import com.puntografico.puntografico.repository.PlantillaCierraBolsasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-cierra-bolsas")
public class PlantillaCierraBolsasRestController {

    @Autowired
    private PlantillaCierraBolsasRepository plantillaCierraBolsasRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoTroqueladoCierraBolsasId,
            @RequestParam Long medidaCierraBolsasId,
            @RequestParam Long cantidadCierraBolsasId) {

        Optional<PlantillaCierraBolsas> plantilla = plantillaCierraBolsasRepository
                .findByTipoTroqueladoCierraBolsas_IdAndMedidaCierraBolsas_IdAndCantidadCierraBolsas_Id(
                        tipoTroqueladoCierraBolsasId, medidaCierraBolsasId, cantidadCierraBolsasId);

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
