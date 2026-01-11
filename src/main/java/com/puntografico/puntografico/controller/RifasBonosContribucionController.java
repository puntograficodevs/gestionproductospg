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
public class RifasBonosContribucionController {

    private final OpcionesRifasContribucionService opcionesRifasContribucionService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final RifasBonosContribucionService rifasBonosContribucionService;

    @GetMapping({"/crear-odt-rifas-bonos-contribucion", "/crear-odt-rifas-bonos-contribucion/{idOrden}"})
    public String verCrearOdtRifasBonosContribucion(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        RifasBonosContribucion rifasBonosContribucion = rifasBonosContribucionService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(RifasBonosContribucion::new);

        List<TipoPapelRifa> listaTipoPapelRifa = opcionesRifasContribucionService.buscarTodosTipoPapelRifa();
        List<TipoTroqueladoRifa> listaTipoTroqueladoRifa = opcionesRifasContribucionService.buscarTodosTipoTroqueladoRifa();
        List<TipoColorRifa> listaTipoColorRifa = opcionesRifasContribucionService.buscarTodosTipoColorRifa();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("rifasBonosContribucion", rifasBonosContribucion);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelRifa", listaTipoPapelRifa);
        model.addAttribute("listaTipoTroqueladoRifa", listaTipoTroqueladoRifa);
        model.addAttribute("listaTipoColorRifa", listaTipoColorRifa);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-rifas-bonos-contribucion";
    }
}
