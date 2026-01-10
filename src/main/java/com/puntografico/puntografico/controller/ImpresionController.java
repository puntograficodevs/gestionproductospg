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
public class ImpresionController {

    private final OpcionesImpresionService opcionesImpresionService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ImpresionService impresionService;

    @GetMapping({"/crear-odt-impresion", "/crear-odt-impresion/{idOrden}"})
    public String verCrearOdtImpresion(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Impresion impresion= impresionService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Impresion::new);

        List<TipoColorImpresion> listaTipoColorImpresion = opcionesImpresionService.buscarTodosTipoColorImpresion();
        List<TamanioHojaImpresion> listaTamanioHojaImpresion = opcionesImpresionService.buscarTodosTamanioHojaImpresion();
        List<TipoFazImpresion> listaTipoFazImpresion = opcionesImpresionService.buscarTodosTipoFazImpresion();
        List<TipoPapelImpresion> listaTipoPapelImpresion = opcionesImpresionService.buscarTodosTipoPapelImpresion();
        List<CantidadImpresion> listaCantidadImpresion = opcionesImpresionService.buscarTodosCantidadImpresion();
        List<TipoImpresion> listaTipoImpresion = opcionesImpresionService.buscarTodosTipoImpresion();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("impresion", impresion);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoColorImpresion", listaTipoColorImpresion);
        model.addAttribute("listaTamanioHojaImpresion", listaTamanioHojaImpresion);
        model.addAttribute("listaTipoFazImpresion", listaTipoFazImpresion);
        model.addAttribute("listaTipoPapelImpresion", listaTipoPapelImpresion);
        model.addAttribute("listaCantidadImpresion", listaCantidadImpresion);
        model.addAttribute("listaTipoImpresion", listaTipoImpresion);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-impresion";
    }
}
