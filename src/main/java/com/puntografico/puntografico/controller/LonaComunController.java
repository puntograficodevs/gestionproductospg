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
public class LonaComunController {

    private final OpcionesLonaComunService opcionesLonaComunService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final LonaComunService lonaComunService;

    @GetMapping({"/crear-odts/crear-odt-lona-comun", "/crear-odts/crear-odt-lona-comun/{idOrden}"})
    public String verCrearOdtLonaComun(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        LonaComun lonaComun = lonaComunService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(LonaComun::new);

        List<MedidaLonaComun> listaMedidaLonaComun = opcionesLonaComunService.buscarTodosMedidaLonaComun();
        List<TipoLonaComun> listaTipoLonaComun = opcionesLonaComunService.buscarTodosTipoLonaComun();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("lonaComun", lonaComun);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaLonaComun", listaMedidaLonaComun);
        model.addAttribute("listaTipoLonaComun", listaTipoLonaComun);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-lona-comun";
    }
}
