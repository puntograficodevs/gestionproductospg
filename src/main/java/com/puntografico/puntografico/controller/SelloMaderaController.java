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
public class SelloMaderaController {

    private final OpcionesSelloMaderaService opcionesSelloMaderaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final SelloMaderaService selloMaderaService;

    @GetMapping({"/crear-odt-sello-madera", "/crear-odt-sello-madera/{idOrden}"})
    public String verCrearOdtSelloMadera(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        SelloMadera selloMadera = selloMaderaService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(SelloMadera::new);

        List<TamanioSelloMadera> listaTamanioSelloMadera = opcionesSelloMaderaService.buscarTodosTamanioSelloMadera();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("selloMadera", selloMadera);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTamanioSelloMadera", listaTamanioSelloMadera);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sello-madera";
    }
}
