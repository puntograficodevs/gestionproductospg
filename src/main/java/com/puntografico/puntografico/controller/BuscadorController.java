package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.service.OrdenTrabajoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class BuscadorController {

    private final OrdenTrabajoService ordenTrabajoService;

    @GetMapping("/buscador")
    public String buscador(HttpSession session, Model model) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");


        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        List<OrdenTrabajo> ordenesEncontradas = new ArrayList<>();
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenesEncontradas", ordenesEncontradas);
        return "buscador";
    }

    @PostMapping("/buscar-orden")
    public String buscarOrden(@RequestParam("datoOrden") String datoOrden, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        List<OrdenTrabajo> ordenesEncontradas = ordenTrabajoService.buscarTodasConIDONombreOTelefono(datoOrden, empleado);

        model.addAttribute("ordenesEncontradas", ordenesEncontradas);
        model.addAttribute("empleado", empleado);

        return "buscador";
    }

    @GetMapping("/buscar-orden")
    public String verBuscador(Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        if (empleado == null) {
            return "redirect:/";
        }
        model.addAttribute("empleado", empleado);
        return "buscador";
    }
}
