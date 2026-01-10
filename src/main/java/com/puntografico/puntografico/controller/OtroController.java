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
public class OtroController {

    private final OpcionesOtroService opcionesOtroService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OtroService otroService;

    @GetMapping({"/crear-odt-otro", "/crear-odt-otro/{idOrden}"})
    public String verCrearOdtOtro(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Otro otro = otroService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Otro::new);

        List<TipoColorOtro> listaTipoColorOtro = opcionesOtroService.buscarTodosTipoColorOtro();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("otro", otro);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoColorOtro", listaTipoColorOtro);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-otro";
    }
}
