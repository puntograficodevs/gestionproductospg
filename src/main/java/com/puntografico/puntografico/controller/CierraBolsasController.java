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
public class CierraBolsasController {

    private final OpcionesCierraBolsasService opcionesCierraBolsasService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CierraBolsasService cierraBolsasService;

    @GetMapping({"/crear-odts/crear-odt-cierra-bolsas", "/crear-odts/crear-odt-cierra-bolsas/{idOrden}"})
    public String verCrearOdtCierraBolsas(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        CierraBolsas cierraBolsas = cierraBolsasService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(CierraBolsas::new);

        List<TipoTroqueladoCierraBolsas> listaTipoTroqueladoCierraBolsas = opcionesCierraBolsasService.buscarTodosTipoTroqueladoCierraBolsas();
        List<MedidaCierraBolsas> listaMedidaCierraBolsas = opcionesCierraBolsasService.buscarTodosMedidaCierraBolsas();
        List<CantidadCierraBolsas> listaCantidadCierraBolsas = opcionesCierraBolsasService.buscarTodosCantidadCierraBolsas();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("cierraBolsas", cierraBolsas);
        model.addAttribute("listaTipoTroqueladoCierraBolsas", listaTipoTroqueladoCierraBolsas);
        model.addAttribute("listaMedidaCierraBolsas", listaMedidaCierraBolsas);
        model.addAttribute("listaCantidadCierraBolsas", listaCantidadCierraBolsas);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-cierra-bolsas";
    }

}
