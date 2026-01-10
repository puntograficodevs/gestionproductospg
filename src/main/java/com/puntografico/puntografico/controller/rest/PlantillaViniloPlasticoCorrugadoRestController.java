package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaViniloPlasticoCorrugado;
import com.puntografico.puntografico.repository.PlantillaViniloPlasticoCorrugadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-vinilo-plastico-corrugado")
public class PlantillaViniloPlasticoCorrugadoRestController {

    @Autowired
    private PlantillaViniloPlasticoCorrugadoRepository plantillaViniloPlasticoCorrugadoRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long medidaViniloPlasticoCorrugadoId
    ) {
        Optional<PlantillaViniloPlasticoCorrugado> plantilla = plantillaViniloPlasticoCorrugadoRepository
                .findByMedidaViniloPlasticoCorrugado_Id(medidaViniloPlasticoCorrugadoId);

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
