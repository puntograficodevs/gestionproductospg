package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.repository.OrdenRepository;
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
 
    private final OrdenRepository ordenRepository;

    @GetMapping("/buscador")
    public String verBuscador(Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        model.addAttribute("ordenesEncontradas", new ArrayList<Orden>());
        model.addAttribute("empleado", empleado);
        return "buscador";
    }

    @PostMapping("/buscar-orden")
    public String buscar(@RequestParam("datoOrden") String datoOrden, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        List<Orden> resultados = (datoOrden == null || datoOrden.isBlank())
                ? new ArrayList<>()
                : ordenRepository.buscarPorCriterioGenerico(datoOrden, empleado.getRol().getId());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenesEncontradas", resultados);
        model.addAttribute("datoBusqueda", datoOrden);
        return "buscador";
    }
}