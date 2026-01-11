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
public class EtiquetaController {

    private final OpcionesEtiquetaService opcionesEtiquetaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final EtiquetaService etiquetaService;

    @GetMapping({"/crear-odts/crear-odt-etiqueta", "/crear-odts/crear-odt-etiqueta/{idOrden}"})
    public String verCrearOdtEtiqueta(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Etiqueta etiqueta = etiquetaService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Etiqueta::new);

        List<TipoPapelEtiqueta> listaTipoPapelEtiqueta = opcionesEtiquetaService.buscarTodosTipoPapelEtiqueta();
        List<TipoLaminadoEtiqueta> listaTipoLaminadoEtiqueta = opcionesEtiquetaService.buscarTodosTipoLaminadoEtiqueta();
        List<TamanioPerforacion> listaTamanioPerforacion = opcionesEtiquetaService.buscarTodosTamanioPerforacion();
        List<TipoFazEtiqueta> listaTipoFazEtiqueta = opcionesEtiquetaService.buscarTodosTipoFazEtiqueta();
        List<CantidadEtiqueta> listaCantidadEtiqueta = opcionesEtiquetaService.buscarTodosCantidadEtiqueta();
        List<MedidaEtiqueta> listaMedidaEtiqueta = opcionesEtiquetaService.buscarTodosMedidaEtiqueta();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("etiqueta", etiqueta);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelEtiqueta", listaTipoPapelEtiqueta);
        model.addAttribute("listaTipoLaminadoEtiqueta", listaTipoLaminadoEtiqueta);
        model.addAttribute("listaTamanioPerforacion", listaTamanioPerforacion);
        model.addAttribute("listaTipoFazEtiqueta", listaTipoFazEtiqueta);
        model.addAttribute("listaCantidadEtiqueta", listaCantidadEtiqueta);
        model.addAttribute("listaMedidaEtiqueta", listaMedidaEtiqueta);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-etiqueta";
    }
}
