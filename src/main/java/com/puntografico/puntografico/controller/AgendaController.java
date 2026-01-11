package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;
    private final MedioPagoService medioPagoService;
    private final OpcionesAgendaService opcionesAgendaService;
    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping({"/crear-odts/crear-odt-agenda", "/crear-odts/crear-odt-agenda/{idOrden}"})
    public String verCrearOdtAgenda(
            HttpSession session,
            Model model,
            @PathVariable(required = false) Long idOrden) {

        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Agenda agenda = agendaService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Agenda::new);

        List<TipoTapaAgenda> listaTipoTapaAgenda = opcionesAgendaService.buscarTodosTipoTapaAgenda();
        List<TipoColorAgenda> listaTipoColorAgenda = opcionesAgendaService.buscarTodosTipoColorAgenda();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("agenda", agenda);
        model.addAttribute("listaTipoTapaAgenda", listaTipoTapaAgenda);
        model.addAttribute("listaTipoColorAgenda", listaTipoColorAgenda);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-agenda";
    }
}
