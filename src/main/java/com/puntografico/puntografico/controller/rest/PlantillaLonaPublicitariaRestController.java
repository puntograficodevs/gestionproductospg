package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaLonaPublicitaria;
import com.puntografico.puntografico.repository.PlantillaLonaPublicitariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-lona-publicitaria")
public class PlantillaLonaPublicitariaRestController {

    @Autowired
    private PlantillaLonaPublicitariaRepository plantillaLonaPublicitariaRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam boolean conAdicionalPortabanner,
            @RequestParam boolean conOjales,
            @RequestParam boolean conOjalesConRefuerzo,
            @RequestParam boolean conBolsillos,
            @RequestParam boolean conDemasiaParaTensado,
            @RequestParam boolean conSolapado,
            @RequestParam Long medidaLonaPublicitariaId,
            @RequestParam Long tipoLonaPublicitariaId
    ) {
        Optional<PlantillaLonaPublicitaria> plantilla = plantillaLonaPublicitariaRepository
                .findByConAdicionalPortabannerAndConOjalesAndConOjalesConRefuerzoAndConBolsillosAndConDemasiaParaTensadoAndConSolapadoAndMedidaLonaPublicitaria_IdAndTipoLonaPublicitaria_Id(
                        conAdicionalPortabanner, conOjales, conOjalesConRefuerzo, conBolsillos, conDemasiaParaTensado, conSolapado, medidaLonaPublicitariaId, tipoLonaPublicitariaId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
