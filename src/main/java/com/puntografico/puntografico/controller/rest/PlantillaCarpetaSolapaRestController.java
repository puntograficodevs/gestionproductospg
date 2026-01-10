package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaCarpetaSolapa;
import com.puntografico.puntografico.repository.PlantillaCarpetaSolapaRepository;
import com.puntografico.puntografico.service.PlantillaCarpetaSolapaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController @AllArgsConstructor
@RequestMapping("/api/plantilla-carpeta-solapa")
public class PlantillaCarpetaSolapaRestController {

    private final PlantillaCarpetaSolapaRepository plantillaCarpetaSolapaRepository;

    private final PlantillaCarpetaSolapaService plantillaCarpetaSolapaService;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam int cantidad,
            @RequestParam Long tipoLaminadoCarpetaSolapaId,
            @RequestParam Long tipoFazCarpetaSolapaId) {

        Long cantidadCarpetaSolapaId = plantillaCarpetaSolapaService.buscarIdCantidadCarpetaSolapa(cantidad);

        Optional<PlantillaCarpetaSolapa> plantilla = plantillaCarpetaSolapaRepository
                .findByTipoLaminadoCarpetaSolapa_IdAndTipoFazCarpetaSolapa_IdAndCantidadCarpetaSolapa_Id(
                        tipoLaminadoCarpetaSolapaId, tipoFazCarpetaSolapaId, cantidadCarpetaSolapaId);

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
