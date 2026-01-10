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
public class ComboController {

    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OpcionesComboService opcionesComboService;
    private final ComboService comboService;

    @GetMapping({"/crear-odt-combo", "/crear-odt-combo/{idOrden}"})
    public String verCrearOdtCombo(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Combo combo = comboService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Combo::new);

        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();
        List<TipoCombo> listaTipoCombo = opcionesComboService.buscarTodosTipoCombo();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("combo", combo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("listaTipoCombo", listaTipoCombo);

        return "crear-odt-combo";
    }
}
