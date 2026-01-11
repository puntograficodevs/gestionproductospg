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
public class SelloAutomaticoController {

    private final OpcionesSelloAutomaticoService opcionesSelloAutomaticoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final SelloAutomaticoService selloAutomaticoService;

    @GetMapping({"/crear-odts/crear-odt-sello-automatico", "/crear-odts/crear-odt-sello-automatico/{idOrden}"})
    public String verCrearOdtSelloAutomatico(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        SelloAutomatico selloAutomatico = selloAutomaticoService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(SelloAutomatico::new);

        List<ModeloSelloAutomatico> listaModeloSelloAutomatico = opcionesSelloAutomaticoService.buscarTodosModeloSelloAutomatico();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("selloAutomatico", selloAutomatico);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaModeloSelloAutomatico", listaModeloSelloAutomatico);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-sello-automatico";
    }
}
