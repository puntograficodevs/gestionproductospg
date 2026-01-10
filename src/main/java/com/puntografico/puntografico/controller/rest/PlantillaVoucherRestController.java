package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.PlantillaVoucher;
import com.puntografico.puntografico.repository.PlantillaVoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantilla-voucher")
public class PlantillaVoucherRestController {

    @Autowired
    private PlantillaVoucherRepository plantillaVoucherRepository;

    @GetMapping("/precio")
    public ResponseEntity<Integer> obtenerPrecio(
            @RequestParam Long medidaVoucherId,
            @RequestParam Long tipoPapelVoucherId,
            @RequestParam Long tipoFazVoucherId,
            @RequestParam Long cantidadVoucherId
    ) {
        Optional<PlantillaVoucher> plantilla = plantillaVoucherRepository
                .findByMedidaVoucher_IdAndTipoPapelVoucher_IdAndTipoFazVoucher_IdAndCantidadVoucher_Id(
                        medidaVoucherId, tipoPapelVoucherId, tipoFazVoucherId, cantidadVoucherId
                );

        return plantilla
                .map(p -> ResponseEntity.ok(p.getPrecio()))
                .orElse(ResponseEntity.noContent().build());
    }
}
