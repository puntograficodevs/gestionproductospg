package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaCombo;
import com.puntografico.puntografico.repository.PlantillaComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-combo")
public class PlantillaComboRestController {

    @Autowired
    private PlantillaComboRepository plantillaComboRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(@RequestParam Long tipoComboId) {
        Optional<PlantillaCombo> plantilla = plantillaComboRepository.findByTipoCombo_Id(tipoComboId);

        return plantilla.map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
