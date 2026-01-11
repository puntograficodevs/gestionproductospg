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
public class TalonarioController {

    private final TalonarioService talonarioService;
    private final MedioPagoService medioPagoService;
    private final OpcionesTalonarioService opcionesTalonarioService;
    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping({"/crear-odts/crear-odt-talonario", "/crear-odts/crear-odt-talonario/{idOrden}"})
    public String verCrearOdtTalonario(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Talonario talonario = talonarioService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Talonario::new);

        List<TipoTalonario> listaTipoTalonario = opcionesTalonarioService.buscarTodosTipoTalonario();
        List<TipoTroqueladoTalonario> listaTipoTroqueladoTalonario = opcionesTalonarioService.buscarTodosTipoTroqueladoTalonario();
        List<ModoTalonario> listaModoTalonario = opcionesTalonarioService.buscarTodosModoTalonario();
        List<TipoColorTalonario> listaTipoColorTalonario = opcionesTalonarioService.buscarTodosTipoColorTalonario();
        List<MedidaTalonario> listaMedidaTalonario = opcionesTalonarioService.buscarTodosMedidaTalonario();
        List<TipoPapelTalonario> listaTipoPapelTalonario = opcionesTalonarioService.buscarTodosTipoPapelTalonario();
        List<CantidadTalonario> listaCantidadTalonario = opcionesTalonarioService.buscarTodosCantidadTalonario();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("talonario", talonario);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoTalonario", listaTipoTalonario);
        model.addAttribute("listaTipoTroqueladoTalonario", listaTipoTroqueladoTalonario);
        model.addAttribute("listaModoTalonario", listaModoTalonario);
        model.addAttribute("listaTipoColorTalonario", listaTipoColorTalonario);
        model.addAttribute("listaMedidaTalonario", listaMedidaTalonario);
        model.addAttribute("listaTipoPapelTalonario", listaTipoPapelTalonario);
        model.addAttribute("listaCantidadTalonario", listaCantidadTalonario);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-talonario";
    }
}
