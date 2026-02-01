package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.GomaPolimeroService;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.OpcionesGomaPolimeroService;
import com.puntografico.puntografico.service.OrdenTrabajoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class GomaPolimeroController {

    private final OpcionesGomaPolimeroService opcionesGomaPolimeroService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final GomaPolimeroService gomaPolimeroService;

    @GetMapping({"/crear-odts/crear-odt-goma-polimero", "/crear-odts/crear-odt-goma-polimero/{idOrden}"})
    public String verCrearOdtGomaPolimero(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        GomaPolimero gomaPolimero = gomaPolimeroService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(GomaPolimero::new);

        List<ModeloGomaPolimero> listaModeloGomaPolimero = opcionesGomaPolimeroService.buscarTodosModeloGomaPolimero();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("gomaPolimero", gomaPolimero);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaModeloGomaPolimero", listaModeloGomaPolimero);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-goma-polimero";
    }
}
