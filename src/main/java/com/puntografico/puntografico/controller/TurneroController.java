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
public class TurneroController {

    private final OpcionesTurneroService opcionesTurneroService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final TurneroService turneroService;

    @GetMapping({"/crear-odt-turnero", "/crear-odt-turnero/{idOrden}"})
    public String verCreadOdtTurnero(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Turnero turnero = turneroService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Turnero::new);

        List<MedidaTurnero> listaMedidaTurnero = opcionesTurneroService.buscarTodosMedidaTurnero();
        List<TipoColorTurnero> listaTipoColorTurnero = opcionesTurneroService.buscarTodosTipoColorTurnero();
        List<CantidadTurnero> listaCantidadTurnero = opcionesTurneroService.buscarTodosCantidadTurnero();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("turnero", turnero);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaTurnero", listaMedidaTurnero);
        model.addAttribute("listaTipoColorTurnero", listaTipoColorTurnero);
        model.addAttribute("listaCantidadTurnero", listaCantidadTurnero);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-turnero";
    }
}
