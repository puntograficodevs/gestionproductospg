package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ListadoController {

    private final OrdenRepository ordenRepository;
    private final MedioPagoService medioPagoService;
    private final ProductoService productoService;

    @GetMapping("/listado")
    public String verListado(HttpSession session, Model model) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        if (empleado == null) return "redirect:/";

        // Traemos absolutamente todas las órdenes de la base
        List<Orden> todas = ordenRepository.findAllByOrderByIdDesc();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();
        List<Producto> productos = productoService.buscarTodos();

        // Las repartimos en las 4 columnas según el ID de estado
        model.addAttribute("ordenesSinHacer", filtrarPorEstado(todas, 1L));
        model.addAttribute("ordenesEnProceso", filtrarPorEstado(todas, 2L));
        model.addAttribute("ordenesListaParaRetirar", filtrarPorEstado(todas, 3L));
        model.addAttribute("ordenesCorregir", filtrarPorEstado(todas, 4L));
        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("productos", productos);

        model.addAttribute("empleado", empleado);

        return "listado";
    }

    private List<Orden> filtrarPorEstado(List<Orden> ordenes, Long estadoId) {
        return ordenes.stream()
                .filter(orden -> orden.getEstadoOrden() != null && orden.getEstadoOrden().getId().equals(estadoId))
                .collect(Collectors.toList());
    }
}