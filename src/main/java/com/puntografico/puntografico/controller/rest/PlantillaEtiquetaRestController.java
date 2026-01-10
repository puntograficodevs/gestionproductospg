package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaEtiqueta;
import com.puntografico.puntografico.repository.PlantillaEtiquetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-etiqueta")
public class PlantillaEtiquetaRestController {

    @Autowired
    private PlantillaEtiquetaRepository plantillaEtiquetaRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long tipoPapelEtiquetaId,
            @RequestParam Long tipoLaminadoEtiquetaId,
            @RequestParam Long tipoFazEtiquetaId,
            @RequestParam Long cantidadEtiquetaId,
            @RequestParam Long medidaEtiquetaId
    ) {
        Optional<PlantillaEtiqueta> plantilla = plantillaEtiquetaRepository
                .findByTipoPapelEtiqueta_IdAndTipoLaminadoEtiqueta_IdAndTipoFazEtiqueta_IdAndCantidadEtiqueta_IdAndMedidaEtiqueta_Id(
                        tipoPapelEtiquetaId, tipoLaminadoEtiquetaId, tipoFazEtiquetaId, cantidadEtiquetaId, medidaEtiquetaId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
