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
public class SublimacionController {

    private final SublimacionService sublimacionService;
    private final MedioPagoService medioPagoService;
    private final OpcionesSublimacionService opcionesSublimacionService;
    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping({"/crear-odts/crear-odt-sublimacion", "/crear-odts/crear-odt-sublimacion/{idOrden}"})
    public String verCrearOdtSublimacion(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Sublimacion sublimacion = sublimacionService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Sublimacion::new);

        List<MaterialSublimacion> listaMaterialSublimacion = opcionesSublimacionService.buscarTodosMaterialSublimacion();
        List<CantidadSublimacion> listaCantidadSublimacion = opcionesSublimacionService.buscarTodosCantidadSublimacion();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("sublimacion", sublimacion);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMaterialSublimacion", listaMaterialSublimacion);
        model.addAttribute("listaCantidadSublimacion", listaCantidadSublimacion);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-sublimacion";
    }
}
