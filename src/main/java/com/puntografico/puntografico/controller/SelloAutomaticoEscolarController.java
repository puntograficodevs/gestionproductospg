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
public class SelloAutomaticoEscolarController {

    private final OpcionesSelloAutomaticoEscolarService opcionesSelloAutomaticoEscolarService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final SelloAutomaticoEscolarService selloAutomaticoEscolarService;

    @GetMapping({"/crear-odts/crear-odt-sello-automatico-escolar", "/crear-odts/crear-odt-sello-automatico-escolar/{idOrden}"})
    public String verCrearOdtSelloAutomaticoEscolar(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        SelloAutomaticoEscolar selloAutomaticoEscolar = selloAutomaticoEscolarService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(SelloAutomaticoEscolar::new);

        List<ModeloSelloAutomaticoEscolar> listaModeloSelloAutomaticoEscolar = opcionesSelloAutomaticoEscolarService.buscarTodosModeloSelloAutomaticoEscolar();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("selloAutomaticoEscolar", selloAutomaticoEscolar);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaModeloSelloAutomaticoEscolar", listaModeloSelloAutomaticoEscolar);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-sello-automatico-escolar";
    }
}
