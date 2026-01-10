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
public class ViniloDeCorteController {

    private final OpcionesViniloDeCorteService opcionesViniloDeCorteService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ViniloDeCorteService viniloDeCorteService;

    @GetMapping({"/crear-odt-vinilo-de-corte", "/crear-odt-vinilo-de-corte/{idOrden}"})
    public String verCrearOdtViniloDeCorte(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        ViniloDeCorte viniloDeCorte = viniloDeCorteService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(ViniloDeCorte::new);

        List<TraeMaterialVinilo> listaTraeMaterialVinilo = opcionesViniloDeCorteService.buscarTodosTraeMaterialVinilo();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("viniloDeCorte", viniloDeCorte);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTraeMaterialVinilo", listaTraeMaterialVinilo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-vinilo-de-corte";
    }
}
