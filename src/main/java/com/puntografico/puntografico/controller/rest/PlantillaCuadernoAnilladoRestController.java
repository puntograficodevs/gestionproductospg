package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaCuadernoAnillado;
import com.puntografico.puntografico.repository.PlantillaCuadernoAnilladoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-cuaderno-anillado")
public class PlantillaCuadernoAnilladoRestController {

    @Autowired
    private PlantillaCuadernoAnilladoRepository plantillaCuadernoAnilladoRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam int cantidadHojas,
            @RequestParam Long tipoTapaCuadernoAnilladoId,
            @RequestParam Long medidaCuadernoAnilladoId) {

        Optional<PlantillaCuadernoAnillado> plantilla = plantillaCuadernoAnilladoRepository
                .findByCantidadHojasAndTipoTapaCuadernoAnillado_IdAndMedidaCuadernoAnillado_Id(
                        cantidadHojas, tipoTapaCuadernoAnilladoId, medidaCuadernoAnilladoId);

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
