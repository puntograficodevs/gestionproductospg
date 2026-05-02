package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.service.MovimientoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping("/ordenes/{idOrden}/historial")
    public String verHistorialMovimientos(@PathVariable Long idOrden, Model model) {
        List<Movimiento> movimientos = movimientoService.buscarPorOrden(idOrden);

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("idOrden", idOrden);

        return "fragments/historial-movimientos :: historialMovimientos";
    }
}
