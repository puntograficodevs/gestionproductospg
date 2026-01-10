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
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final MedioPagoService medioPagoService;
    private final OpcionesTarjetaService opcionesTarjetaService;
    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping({"/crear-odt-tarjeta", "/crear-odt-tarjeta/{idOrden}"})
    public String verCrearOdtTarjeta(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Tarjeta tarjeta = tarjetaService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Tarjeta::new);

        List<TipoPapelTarjeta> listaTipoPapelTarjeta = opcionesTarjetaService.buscarTodosTipoPapelTarjeta();
        List<TipoColorTarjeta> listaTipoColorTarjeta = opcionesTarjetaService.buscarTodosTipoColorTarjeta();
        List<TipoFazTarjeta> listaTipoFazTarjeta = opcionesTarjetaService.buscarTodosTipoFazTarjeta();
        List<TipoLaminadoTarjeta> listaTipoLaminadoTarjeta = opcionesTarjetaService.buscarTodosTipoLaminadoTarjeta();
        List<MedidaTarjeta> listaMedidaTarjeta = opcionesTarjetaService.buscarTodosMedidaTarjeta();
        List<CantidadTarjeta> listaCantidadTarjeta = opcionesTarjetaService.buscarTodosCantidadTarjeta();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("tarjeta", tarjeta);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelTarjeta", listaTipoPapelTarjeta);
        model.addAttribute("listaTipoColorTarjeta", listaTipoColorTarjeta);
        model.addAttribute("listaTipoFazTarjeta", listaTipoFazTarjeta);
        model.addAttribute("listaTipoLaminadoTarjeta", listaTipoLaminadoTarjeta);
        model.addAttribute("listaMedidaTarjeta", listaMedidaTarjeta);
        model.addAttribute("listaCantidadTarjeta", listaCantidadTarjeta);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-tarjeta";
    }
}
