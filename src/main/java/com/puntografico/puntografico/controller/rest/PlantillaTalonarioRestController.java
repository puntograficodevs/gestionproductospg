package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaTalonario;
import com.puntografico.puntografico.repository.PlantillaTalonarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-talonario")
public class PlantillaTalonarioRestController {

    @Autowired
    private PlantillaTalonarioRepository plantillaTalonarioRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam int cantidadHojas,
            @RequestParam boolean conNumerado,
            @RequestParam Long tipoTalonarioId,
            @RequestParam Long tipoTroqueladoTalonarioId,
            @RequestParam Long modoTalonarioId,
            @RequestParam Long tipoColorTalonarioId,
            @RequestParam Long medidaTalonarioId,
            @RequestParam Long tipoPapelTalonarioId,
            @RequestParam Long cantidadTalonarioId
    ) {
        Optional<PlantillaTalonario> plantilla = plantillaTalonarioRepository
                .findByCantidadHojasAndConNumeradoAndTipoTalonario_IdAndTipoTroqueladoTalonario_IdAndModoTalonario_IdAndTipoColorTalonario_IdAndMedidaTalonario_IdAndTipoPapelTalonario_IdAndCantidadTalonario_Id(
                        cantidadHojas, conNumerado, tipoTalonarioId, tipoTroqueladoTalonarioId, modoTalonarioId, tipoColorTalonarioId, medidaTalonarioId, tipoPapelTalonarioId, cantidadTalonarioId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
