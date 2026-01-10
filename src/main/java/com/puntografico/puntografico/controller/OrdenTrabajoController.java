package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.service.OrdenTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @GetMapping("/crear-orden")
    public String verCrearOrden(HttpSession session, Model model) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        model.addAttribute("empleado", empleado);

        return "crear-orden";
    }

    @PostMapping("/api/orden/pedir-correccion")
    public String guardarCorreccion(HttpServletRequest request) {
        Long idOrdenTrabajo = Long.parseLong(request.getParameter("idOrden"));
        String correccion = request.getParameter("correccion");
        ordenTrabajoService.guardarCorreccion(idOrdenTrabajo, correccion);
        ordenTrabajoService.cambiarEstadoACorregir(idOrdenTrabajo);

        return "redirect:/listado";
    }
}
