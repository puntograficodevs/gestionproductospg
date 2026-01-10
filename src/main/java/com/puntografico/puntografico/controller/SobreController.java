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
public class SobreController {

    private final SobreService sobreService;
    private final MedioPagoService medioPagoService;
    private final OpcionesSobreService opcionesSobreService;
    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping({"/crear-odt-sobre", "/crear-odt-sobre/{idOrden}"})
    public String verCrearOdtSobre(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Sobre sobre = sobreService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Sobre::new);

        List<MedidaSobre> listaMedidaSobre = opcionesSobreService.buscarTodosMedidaSobre();
        List<TipoColorSobre> listaTipoColorSobre = opcionesSobreService.buscarTodosTipoColorSobre();
        List<CantidadSobre> listaCantidadSobre = opcionesSobreService.buscarTodosCantidadSobre();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("sobre", sobre);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaSobre", listaMedidaSobre);
        model.addAttribute("listaTipoColorSobre", listaTipoColorSobre);
        model.addAttribute("listaCantidadSobre", listaCantidadSobre);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sobre";
    }
}
