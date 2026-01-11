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
public class CatalogoController {

    private final OpcionesCatalogoService opcionesCatalogoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CatalogoService catalogoService;

    @GetMapping({"/crear-odts/crear-odt-catalogo", "/crear-odts/crear-odt-catalogo/{idOrden}"})
    public String verCrearOdtCatalogo(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Catalogo catalogo = catalogoService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Catalogo::new);


        List<TipoFazCatalogo> listaTipoFazCatalogo = opcionesCatalogoService.buscarTodosTipoFazCatalogo();
        List<TipoLaminadoCatalogo> listaTipoLaminadoCatalogo = opcionesCatalogoService.buscarTodosTipoLaminadoCatalogo();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("catalogo", catalogo);
        model.addAttribute("listaTipoFazCatalogo", listaTipoFazCatalogo);
        model.addAttribute("listaTipoLaminadoCatalogo", listaTipoLaminadoCatalogo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-catalogo";
    }
}
