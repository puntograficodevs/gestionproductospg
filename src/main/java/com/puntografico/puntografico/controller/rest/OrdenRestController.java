package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor
@RequestMapping("/api/orden")
public class OrdenRestController {

    private final OrdenAgendaService ordenAgendaService;

    private final OrdenAnotadorService ordenAnotadorService;

    private final OrdenCarpetaSolapaService ordenCarpetaSolapaService;

    private final OrdenCatalogoService ordenCatalogoService;

    private final OrdenCierraBolsasService ordenCierraBolsasService;

    private final OrdenComboService ordenComboService;

    private final OrdenCuadernoAnilladoService ordenCuadernoAnilladoService;

    private final OrdenEntradaService ordenEntradaService;

    private final OrdenEtiquetaService ordenEtiquetaService;

    private final OrdenFlybannerService ordenFlybannerService;

    private final OrdenFolletoService ordenFolletoService;

    private final OrdenHojasMembreteadasService ordenHojasMembreteadasService;

    private final OrdenImpresionService ordenImpresionService;

    private final OrdenLonaComunService ordenLonaComunService;

    private final OrdenLonaPublicitariaService ordenLonaPublicitariaService;

    private final OrdenOtroService ordenOtroService;

    private final OrdenRifasBonosContribucionService ordenRifasBonosContribucionService;

    private final OrdenRotulacionService ordenRotulacionService;

    private final OrdenSelloAutomaticoService ordenSelloAutomaticoService;

    private final OrdenSelloAutomaticoEscolarService ordenSelloAutomaticoEscolarService;

    private final OrdenSelloMaderaService ordenSelloMaderaService;

    private final OrdenSobreService ordenSobreService;

    private final OrdenStickerService ordenStickerService;

    private final OrdenSublimacionService ordenSublimacionService;

    private final OrdenTalonarioService ordenTalonarioService;

    private final OrdenTarjetaService ordenTarjetaService;

    private final OrdenTurneroService ordenTurneroService;

    private final OrdenViniloService ordenViniloService;

    private final OrdenViniloDeCorteService ordenViniloDeCorteService;

    private final OrdenViniloPlasticoCorrugadoService ordenViniloPlasticoCorrugadoService;

    private final OrdenVoucherService ordenVoucherService;

    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping("/ordenAgenda/{ordenId}")
    public ResponseEntity<OrdenAgenda> getOrdenAgenda(@PathVariable Long ordenId) {
        OrdenAgenda ordenAgenda = ordenAgendaService.buscarPorOrdenId(ordenId);

        if (ordenAgenda != null) {
            return ResponseEntity.ok(ordenAgenda);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenAnotador/{ordenId}")
    public ResponseEntity<OrdenAnotador> getOrdenAnotador(@PathVariable Long ordenId) {
        OrdenAnotador ordenAnotador = ordenAnotadorService.buscarPorOrdenId(ordenId);

        if (ordenAnotador != null) {
            return ResponseEntity.ok(ordenAnotador);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenCarpetaSolapa/{ordenId}")
    public ResponseEntity<OrdenCarpetaSolapa> getOrdenCarpetaSolapa(@PathVariable Long ordenId) {
        OrdenCarpetaSolapa ordenCarpetaSolapa = ordenCarpetaSolapaService.buscarPorOrdenId(ordenId);

        if (ordenCarpetaSolapa != null) {
            return ResponseEntity.ok(ordenCarpetaSolapa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenCatalogo/{ordenId}")
    public ResponseEntity<OrdenCatalogo> getOrdenCatalogo(@PathVariable Long ordenId) {
        OrdenCatalogo ordenCatalogo = ordenCatalogoService.buscarPorOrdenId(ordenId);

        if (ordenCatalogo != null) {
            return ResponseEntity.ok(ordenCatalogo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenCierraBolsas/{ordenId}")
    public ResponseEntity<OrdenCierraBolsas> getOrdenCierraBolsas(@PathVariable Long ordenId) {
        OrdenCierraBolsas ordenCierraBolsas = ordenCierraBolsasService.buscarPorOrdenId(ordenId);

        if (ordenCierraBolsas != null) {
            return ResponseEntity.ok(ordenCierraBolsas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenCombo/{ordenId}")
    public ResponseEntity<OrdenCombo> getOrdenCombo(@PathVariable Long ordenId) {
        OrdenCombo ordenCombo = ordenComboService.buscarPorOrdenId(ordenId);

        if (ordenCombo != null) {
            return ResponseEntity.ok(ordenCombo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenCuadernoAnillado/{ordenId}")
    public ResponseEntity<OrdenCuadernoAnillado> getOrdenCuadernoAnillado(@PathVariable Long ordenId) {
        OrdenCuadernoAnillado ordenCuadernoAnillado = ordenCuadernoAnilladoService.buscarPorOrdenId(ordenId);

        if (ordenCuadernoAnillado != null) {
            return ResponseEntity.ok(ordenCuadernoAnillado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenEntrada/{ordenId}")
    public ResponseEntity<OrdenEntrada> getOrdenEntrada(@PathVariable Long ordenId) {
        OrdenEntrada ordenEntrada = ordenEntradaService.buscarPorOrdenId(ordenId);

        if (ordenEntrada != null) {
            return ResponseEntity.ok(ordenEntrada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenEtiqueta/{ordenId}")
    public ResponseEntity<OrdenEtiqueta> getOrdenEtiqueta(@PathVariable Long ordenId) {
        OrdenEtiqueta ordenEtiqueta = ordenEtiquetaService.buscarPorOrdenId(ordenId);

        if (ordenEtiqueta != null) {
            return ResponseEntity.ok(ordenEtiqueta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenFlybanner/{ordenId}")
    public ResponseEntity<OrdenFlybanner> getOrdenFlybanner(@PathVariable Long ordenId) {
        OrdenFlybanner ordenFlybanner = ordenFlybannerService.buscarPorOrdenId(ordenId);

        if (ordenFlybanner != null) {
            return ResponseEntity.ok(ordenFlybanner);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenFolleto/{ordenId}")
    public ResponseEntity<OrdenFolleto> getOrdenFolleto(@PathVariable Long ordenId) {
        OrdenFolleto ordenFolleto = ordenFolletoService.buscarPorOrdenId(ordenId);

        if (ordenFolleto != null) {
            return ResponseEntity.ok(ordenFolleto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenHojasMembreteadas/{ordenId}")
    public ResponseEntity<OrdenHojasMembreteadas> getOrdenHojasMembreteadas(@PathVariable Long ordenId) {
        OrdenHojasMembreteadas ordenHojasMembreteadas = ordenHojasMembreteadasService.buscarPorOrdenId(ordenId);

        if (ordenHojasMembreteadas != null) {
            return ResponseEntity.ok(ordenHojasMembreteadas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenImpresion/{ordenId}")
    public ResponseEntity<OrdenImpresion> getOrdenImpresion(@PathVariable Long ordenId) {
        OrdenImpresion ordenImpresion = ordenImpresionService.buscarPorOrdenId(ordenId);

        if (ordenImpresion != null) {
            return ResponseEntity.ok(ordenImpresion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenLonaComun/{ordenId}")
    public ResponseEntity<OrdenLonaComun> getOrdenLonaComun(@PathVariable Long ordenId) {
        OrdenLonaComun ordenLonaComun = ordenLonaComunService.buscarPorOrdenId(ordenId);

        if (ordenLonaComun != null) {
            return ResponseEntity.ok(ordenLonaComun);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenLonaPublicitaria/{ordenId}")
    public ResponseEntity<OrdenLonaPublicitaria> getOrdenLonaPublicitaria(@PathVariable Long ordenId) {
        OrdenLonaPublicitaria ordenLonaPublicitaria = ordenLonaPublicitariaService.buscarPorOrdenId(ordenId);

        if (ordenLonaPublicitaria != null) {
            return ResponseEntity.ok(ordenLonaPublicitaria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenOtro/{ordenId}")
    public ResponseEntity<OrdenOtro> getOrdenOtro(@PathVariable Long ordenId) {
        OrdenOtro ordenOtro = ordenOtroService.buscarPorOrdenId(ordenId);

        if (ordenOtro != null) {
            return ResponseEntity.ok(ordenOtro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenRifa/{ordenId}")
    public ResponseEntity<OrdenRifasBonosContribucion> getOrdenRifasBonosContribucion(@PathVariable Long ordenId) {
        OrdenRifasBonosContribucion ordenRifa = ordenRifasBonosContribucionService.buscarPorOrdenId(ordenId);

        if (ordenRifa != null) {
            return ResponseEntity.ok(ordenRifa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenRotulacion/{ordenId}")
    public ResponseEntity<OrdenRotulacion> getOrdenRotulacion(@PathVariable Long ordenId) {
        OrdenRotulacion ordenRotulacion = ordenRotulacionService.buscarPorOrdenId(ordenId);

        if (ordenRotulacion != null) {
            return ResponseEntity.ok(ordenRotulacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenSelloAutomatico/{ordenId}")
    public ResponseEntity<OrdenSelloAutomatico> getOrdenSelloAutomatico(@PathVariable Long ordenId) {
        OrdenSelloAutomatico ordenSelloAutomatico = ordenSelloAutomaticoService.buscarPorOrdenId(ordenId);

        if (ordenSelloAutomatico != null) {
            return ResponseEntity.ok(ordenSelloAutomatico);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenSelloAutomaticoEscolar/{ordenId}")
    public ResponseEntity<OrdenSelloAutomaticoEscolar> getOrdenSelloAutomaticoEscolar(@PathVariable Long ordenId) {
        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolar = ordenSelloAutomaticoEscolarService.buscarPorOrdenId(ordenId);

        if (ordenSelloAutomaticoEscolar != null) {
            return ResponseEntity.ok(ordenSelloAutomaticoEscolar);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenSelloMadera/{ordenId}")
    public ResponseEntity<OrdenSelloMadera> getOrdenSelloMadera(@PathVariable Long ordenId) {
        OrdenSelloMadera ordenSelloMadera = ordenSelloMaderaService.buscarPorOrdenId(ordenId);

        if (ordenSelloMadera != null) {
            return ResponseEntity.ok(ordenSelloMadera);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenSobre/{ordenId}")
    public ResponseEntity<OrdenSobre> getOrdenSobre(@PathVariable Long ordenId) {
        OrdenSobre ordenSobre = ordenSobreService.buscarPorOrdenId(ordenId);

        if (ordenSobre != null) {
            return ResponseEntity.ok(ordenSobre);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenSticker/{ordenId}")
    public ResponseEntity<OrdenSticker> getOrdenSticker(@PathVariable Long ordenId) {
        OrdenSticker ordenSticker = ordenStickerService.buscarPorOrdenId(ordenId);

        if (ordenSticker != null) {
            return ResponseEntity.ok(ordenSticker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenSublimacion/{ordenId}")
    public ResponseEntity<OrdenSublimacion> getOrdenSublimacion(@PathVariable Long ordenId) {
        OrdenSublimacion ordenSublimacion = ordenSublimacionService.buscarPorOrdenId(ordenId);

        if (ordenSublimacion != null) {
            return ResponseEntity.ok(ordenSublimacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenTalonario/{ordenId}")
    public ResponseEntity<OrdenTalonario> getOrdenTalonario(@PathVariable Long ordenId) {
        OrdenTalonario ordenTalonario = ordenTalonarioService.buscarPorOrdenId(ordenId);

        if (ordenTalonario != null) {
            return ResponseEntity.ok(ordenTalonario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenTarjeta/{ordenId}")
    public ResponseEntity<OrdenTarjeta> getOrdenTarjeta(@PathVariable Long ordenId) {
        OrdenTarjeta ordenTarjeta = ordenTarjetaService.buscarPorOrdenId(ordenId);

        if (ordenTarjeta != null) {
            return ResponseEntity.ok(ordenTarjeta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenTurnero/{ordenId}")
    public ResponseEntity<OrdenTurnero> getOrdenTurnero(@PathVariable Long ordenId) {
        OrdenTurnero ordenTurnero = ordenTurneroService.buscarPorOrdenId(ordenId);

        if (ordenTurnero != null) {
            return ResponseEntity.ok(ordenTurnero);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenVinilo/{ordenId}")
    public ResponseEntity<OrdenVinilo> getOrdenVinilo(@PathVariable Long ordenId) {
        OrdenVinilo ordenVinilo = ordenViniloService.buscarPorOrdenId(ordenId);

        if (ordenVinilo != null) {
            return ResponseEntity.ok(ordenVinilo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenViniloPlasticoCorrugado/{ordenId}")
    public ResponseEntity<OrdenViniloPlasticoCorrugado> getOrdenViniloPlasticoCorrugado(@PathVariable Long ordenId) {
        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugado = ordenViniloPlasticoCorrugadoService.buscarPorOrdenId(ordenId);

        if (ordenViniloPlasticoCorrugado != null) {
            return ResponseEntity.ok(ordenViniloPlasticoCorrugado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenViniloDeCorte/{ordenId}")
    public ResponseEntity<OrdenViniloDeCorte> getOrdenViniloDeCorte(@PathVariable Long ordenId) {
        OrdenViniloDeCorte ordenViniloDeCorte = ordenViniloDeCorteService.buscarPorOrdenId(ordenId);

        if (ordenViniloDeCorte != null) {
            return ResponseEntity.ok(ordenViniloDeCorte);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ordenVoucher/{ordenId}")
    public ResponseEntity<OrdenVoucher> getOrdenVoucher(@PathVariable Long ordenId) {
        OrdenVoucher ordenVoucher = ordenVoucherService.buscarPorOrdenId(ordenId);

        if (ordenVoucher != null) {
            return ResponseEntity.ok(ordenVoucher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
