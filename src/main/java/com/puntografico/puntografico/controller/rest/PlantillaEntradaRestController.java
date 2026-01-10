package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaEntrada;
import com.puntografico.puntografico.repository.PlantillaEntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-entrada")
public class PlantillaEntradaRestController {

    @Autowired
    private PlantillaEntradaRepository plantillaEntradaRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoPapelEntradaId,
            @RequestParam Long tipoColorEntradaId,
            @RequestParam Long tipoTroqueladoEntradaId,
            @RequestParam Long medidaEntradaId,
            @RequestParam Long cantidadEntradaId,
            @RequestParam Long numeradoEntradaId,
            @RequestParam Long terminacionEntradaId) {

        Optional<PlantillaEntrada> plantilla = plantillaEntradaRepository
                .findByTipoPapelEntrada_IdAndTipoColorEntrada_IdAndTipoTroqueladoEntrada_IdAndMedidaEntrada_IdAndCantidadEntrada_IdAndNumeradoEntrada_IdAndTerminacionEntrada_Id(
                        tipoPapelEntradaId, tipoColorEntradaId, tipoTroqueladoEntradaId, medidaEntradaId, cantidadEntradaId, numeradoEntradaId, terminacionEntradaId);

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
