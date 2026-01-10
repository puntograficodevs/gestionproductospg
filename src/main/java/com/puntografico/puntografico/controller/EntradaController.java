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
public class EntradaController {

    private final OpcionesEntradaService opcionesEntradaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final EntradaService entradaService;

    @GetMapping({"/crear-odt-entrada", "/crear-odt-entrada/{idOrden}"})
    public String verCrearOdtEntrada(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Entrada entrada = entradaService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Entrada::new);

        List<TipoPapelEntrada> listaTipoPapelEntrada = opcionesEntradaService.buscarTodosTipoPapelEntrada();
        List<TipoColorEntrada> listaTipoColorEntrada = opcionesEntradaService.buscarTodosTipoColorEntrada();
        List<TipoTroqueladoEntrada> listaTipoTroqueladoEntrada = opcionesEntradaService.buscarTodosTipoTroqueladoEntrada();
        List<MedidaEntrada> listaMedidaEntrada = opcionesEntradaService.buscarTodosMedidaEntrada();
        List<CantidadEntrada> listaCantidadEntrada = opcionesEntradaService.buscarTodosCantidadEntrada();
        List<NumeradoEntrada> listaNumeradoEntrada = opcionesEntradaService.buscarTodosNumeradoEntrada();
        List<TerminacionEntrada> listaTerminacionEntrada = opcionesEntradaService.buscarTodosTerminacionEntrada();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("entrada", entrada);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelEntrada", listaTipoPapelEntrada);
        model.addAttribute("listaTipoColorEntrada", listaTipoColorEntrada);
        model.addAttribute("listaTipoTroqueladoEntrada", listaTipoTroqueladoEntrada);
        model.addAttribute("listaMedidaEntrada", listaMedidaEntrada);
        model.addAttribute("listaCantidadEntrada", listaCantidadEntrada);
        model.addAttribute("listaNumeradoEntrada", listaNumeradoEntrada);
        model.addAttribute("listaTerminacionEntrada", listaTerminacionEntrada);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-entrada";
    }
}
