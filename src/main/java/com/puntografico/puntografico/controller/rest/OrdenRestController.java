package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor
@RequestMapping("/api/orden")
public class OrdenRestController {

    private final OrdenTrabajoService ordenTrabajoService;

    @PostMapping("/cambiar-a-corregir/{ordenId}")
    public ResponseEntity<Void> cambiarEstadoACorregir(@PathVariable Long ordenId) {
        ordenTrabajoService.cambiarEstadoACorregir(ordenId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cambiar-a-en-proceso/{ordenId}")
    public ResponseEntity<Void> cambiarEstadoAEnProceso(@PathVariable Long ordenId) {
        ordenTrabajoService.cambiarEstadoAEnProceso(ordenId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cambiar-a-lista-para-retirar/{ordenId}")
    public ResponseEntity<Void> cambiarEstadoAListaParaRetirar(@PathVariable Long ordenId) {
        ordenTrabajoService.cambiarEstadoAListaParaRetirar(ordenId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cambiar-a-retirada/{ordenId}")
    public ResponseEntity<Void> cambiarEstadoARetirada(@PathVariable Long ordenId) {
        ordenTrabajoService.cambiarEstadoARetirada(ordenId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cambiar-a-sin-hacer/{ordenId}")
    public ResponseEntity<Void> cambiarEstadoASinHacer(@PathVariable Long ordenId) {
        ordenTrabajoService.cambiarEstadoASinHacer(ordenId);
        return ResponseEntity.ok().build();
    }
}
