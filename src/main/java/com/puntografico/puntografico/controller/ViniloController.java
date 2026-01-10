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
public class ViniloController {

    private final OpcionesViniloService opcionesViniloService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ViniloService viniloService;

    @GetMapping({"/crear-odt-vinilo", "/crear-odt-vinilo/{idOrden}"})
    public String verCrearOdtVinilo(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Vinilo vinilo = viniloService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Vinilo::new);

        List<TipoVinilo> listaTipoVinilo = opcionesViniloService.buscarTodosTipoVinilo();
        List<TipoAdicionalVinilo> listaTipoAdicionalVinilo = opcionesViniloService.buscarTodosTipoAdicionalVinilo();
        List<TipoCorteVinilo> listaTipoCorteVinilo = opcionesViniloService.buscarTodosTipoCorteVinilo();
        List<MedidaVinilo> listaMedidaVinilo = opcionesViniloService.buscarTodosMedidaVinilo();
        List<CantidadVinilo> listaCantidadVinilo = opcionesViniloService.buscarTodosCantidadVinilo();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("vinilo", vinilo);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoVinilo", listaTipoVinilo);
        model.addAttribute("listaTipoAdicionalVinilo", listaTipoAdicionalVinilo);
        model.addAttribute("listaTipoCorteVinilo", listaTipoCorteVinilo);
        model.addAttribute("listaMedidaVinilo", listaMedidaVinilo);
        model.addAttribute("listaCantidadVinilo", listaCantidadVinilo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-vinilo";
    }
}
