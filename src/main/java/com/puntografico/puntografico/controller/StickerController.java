package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class StickerController {

    private final StickerService stickerService;
    private final MedioPagoService medioPagoService;
    private final OpcionesStickerService opcionesStickerService;
    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping({"/crear-odts/crear-odt-sticker", "/crear-odts/crear-odt-sticker/{idOrden}"})
    public String verCrearOdtSticker(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Sticker sticker = stickerService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Sticker::new);

        List<TipoTroqueladoSticker> listaTipoTroqueladoSticker = opcionesStickerService.buscarTodosTipoTroqueladoSticker();
        List<CantidadSticker> listaCantidadSticker = opcionesStickerService.buscarTodosCantidadSticker();
        List<MedidaSticker> listaMedidaSticker = opcionesStickerService.buscarTodosMedidaSticker();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("sticker", sticker);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoTroqueladoSticker", listaTipoTroqueladoSticker);
        model.addAttribute("listaCantidadSticker", listaCantidadSticker);
        model.addAttribute("listaMedidaSticker", listaMedidaSticker);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-sticker";
    }
}
