package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaLonaComun;
import com.puntografico.puntografico.repository.PlantillaLonaComunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-lona-comun")
public class PlantillaLonaComunRestController {

    @Autowired
    private PlantillaLonaComunRepository plantillaLonaComunRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam boolean conBolsillos,
            @RequestParam Long medidaLonaComunId,
            @RequestParam Long tipoLonaComunId
    ) {
        Optional<PlantillaLonaComun> plantilla = plantillaLonaComunRepository.
                findByConBolsillosAndMedidaLonaComun_IdAndTipoLonaComun_Id(
                        conBolsillos, medidaLonaComunId, tipoLonaComunId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
