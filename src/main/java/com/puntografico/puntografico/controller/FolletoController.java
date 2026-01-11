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
public class FolletoController {

    private final OpcionesFolletoService opcionesFolletoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final FolletoService folletoService;

    @GetMapping({"/crear-odts/crear-odt-folleto", "/crear-odts/crear-odt-folleto/{idOrden}"})
    public String verCrearOdtFolleto(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Folleto folleto = folletoService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Folleto::new);

        List<TipoPapelFolleto> listaTipoPapelFolleto = opcionesFolletoService.buscarTodosTipoPapelFolleto();
        List<TipoColorFolleto> listaTipoColorFolleto = opcionesFolletoService.buscarTodosTipoColorFolleto();
        List<TipoFazFolleto> listaTipoFazFolleto = opcionesFolletoService.buscarTodosTipoFazFolleto();
        List<TamanioHojaFolleto> listaTamanioHojaFolleto = opcionesFolletoService.buscarTodosTamanioHojaFolleto();
        List<TipoFolleto> listaTipoFolleto = opcionesFolletoService.buscarTodosTipoFolleto();
        List<CantidadFolleto> listaCantidadFolleto = opcionesFolletoService.buscarTodosCantidadFolleto();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("folleto", folleto);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelFolleto", listaTipoPapelFolleto);
        model.addAttribute("listaTipoColorFolleto", listaTipoColorFolleto);
        model.addAttribute("listaTipoFazFolleto", listaTipoFazFolleto);
        model.addAttribute("listaTamanioHojaFolleto", listaTamanioHojaFolleto);
        model.addAttribute("listaTipoFolleto", listaTipoFolleto);
        model.addAttribute("listaCantidadFolleto", listaCantidadFolleto);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-folleto";
    }
}
