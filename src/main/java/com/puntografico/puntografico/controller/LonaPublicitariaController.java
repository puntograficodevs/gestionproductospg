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
public class LonaPublicitariaController {

    private final OpcionesLonaPublicitariaService opcionesLonaPublicitariaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final LonaPublicitariaService lonaPublicitariaService;

    @GetMapping({"/crear-odts/crear-odt-lona-publicitaria", "/crear-odts/crear-odt-lona-publicitaria/{idOrden}"})
    public String verCrearOdtLonaPublicitaria(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        LonaPublicitaria lonaPublicitaria = lonaPublicitariaService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(LonaPublicitaria::new);

        List<MedidaLonaPublicitaria> listaMedidaLonaPublicitaria = opcionesLonaPublicitariaService.buscarTodosMedidaLonaPublicitaria();
        List<TipoLonaPublicitaria> listaTipoLonaPublicitaria = opcionesLonaPublicitariaService.buscarTodosTipoLonaPublicitaria();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("lonaPublicitaria", lonaPublicitaria);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaLonaPublicitaria", listaMedidaLonaPublicitaria);
        model.addAttribute("listaTipoLonaPublicitaria", listaTipoLonaPublicitaria);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-lona-publicitaria";
    }
}
