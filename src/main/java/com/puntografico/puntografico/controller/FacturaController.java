package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.repository.OrdenRepository;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/facturas")
@AllArgsConstructor
public class FacturaController {

    private final OrdenRepository ordenRepository;

    @GetMapping
    public String listarPendientes(Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        if (empleado == null) return "redirect:/";

        List<Orden> pendientes = ordenRepository.findByNecesitaFacturaTrueAndFacturaHechaFalse();
        model.addAttribute("ordenes", pendientes);
        model.addAttribute("empleado", empleado);

        return "facturas-pendientes";
    }

    @PostMapping("/marcar-hecha/{id}")
    public String marcarComoFacturada(@PathVariable Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setFacturaHecha(true);
        ordenRepository.save(orden);

        return "redirect:/facturas";
    }
}
