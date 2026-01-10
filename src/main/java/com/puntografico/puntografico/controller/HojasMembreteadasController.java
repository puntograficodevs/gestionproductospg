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
public class HojasMembreteadasController {

    private final OpcionesHojasMembreteadasService opcionesHojasMembreteadasService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final HojasMembreteadasService hojasMembreteadasService;

    @GetMapping({"/crear-odt-hojas-membreteadas", "/crear-odt-hojas-membreteadas/{idOrden}"})
    public String verCreadOdtHojasMembreteadas(Model model,
                                               HttpSession session,
                                               @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        HojasMembreteadas hojasMembreteadas = hojasMembreteadasService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(HojasMembreteadas::new);

        List<MedidaHojasMembreteadas> listaMedidaHojasMembreteadas = opcionesHojasMembreteadasService.buscarTodosMedidaHojasMembreteadas();
        List<TipoColorHojasMembreteadas> listaTipoColorHojasMembreteadas = opcionesHojasMembreteadasService.buscarTodosTipoColorHojasMembreteadas();
        List<CantidadHojasMembreteadas> listaCantidadHojasMembreteadas = opcionesHojasMembreteadasService.buscarTodosCantidadHojasMembreteadas();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("hojasMembreteadas", hojasMembreteadas);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaHojasMembreteadas", listaMedidaHojasMembreteadas);
        model.addAttribute("listaTipoColorHojasMembreteadas", listaTipoColorHojasMembreteadas);
        model.addAttribute("listaCantidadHojasMembreteadas", listaCantidadHojasMembreteadas);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-hojas-membreteadas";
    }
}
