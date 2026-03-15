package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.service.OrdenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class BuscadorController {
 
    private final OrdenService ordenService;

    @GetMapping("/buscador")
    public String verBuscador(Model model, HttpSession session) {
        model.addAttribute("ordenesEncontradas", new ArrayList<Orden>());
        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));
        return "buscador";
    }

    @PostMapping("/buscar-orden")
    public String buscar(@RequestParam("datoOrden") String datoOrden, Model model, HttpSession session) {
        Empleado empleadoLogueado = (Empleado) session.getAttribute("empleadoLogueado");

        List<Orden> resultados = (datoOrden == null || datoOrden.isBlank())
                ? new ArrayList<>()
                : ordenService.buscarPorCriterioGenerico(datoOrden, empleadoLogueado.getRol().getId());

        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));
        model.addAttribute("ordenesEncontradas", resultados);
        model.addAttribute("datoBusqueda", datoOrden);
        return "buscador";
    }
}