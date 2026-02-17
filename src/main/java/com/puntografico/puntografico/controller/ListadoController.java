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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ListadoController {

    private final OrdenRepository ordenRepository;
    private final MedioPagoService medioPagoService;
    private final ProductoService productoService;

    @GetMapping("/listado")
    public String verListado(@RequestParam(value = "producto", required = false) String productoFiltro, HttpSession session, Model model) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        if (empleado == null) return "redirect:/";

        List<Orden> todas = ordenRepository.buscarTodasSegunRol(empleado.getRol().getId());
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        // 1. Obtenemos y ordenamos la lista de productos (A-Z, "Sin categoria" al final)
        List<Producto> productos = productoService.buscarTodos().stream()
                .sorted(crearComparadorProductos())
                .collect(Collectors.toList());

        // 2. Repartimos las órdenes filtradas por estado y TAMBIÉN las ordenamos A-Z por producto
        model.addAttribute("filtroSeleccionado", productoFiltro != null ? productoFiltro : "todas");
        model.addAttribute("ordenesSinHacer", filtrarYOrdenar(todas, 1L, empleado));
        model.addAttribute("ordenesEnProceso", filtrarYOrdenar(todas, 2L, empleado));
        model.addAttribute("ordenesListaParaRetirar", filtrarYOrdenar(todas, 3L, empleado));
        model.addAttribute("ordenesCorregir", filtrarYOrdenar(todas, 4L, empleado));

        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("productos", productos);
        model.addAttribute("empleado", empleado);

        return "listado";
    }

    private List<Orden> filtrarYOrdenar(List<Orden> ordenes, Long estadoId, Empleado empleado) {
        return ordenes.stream()
                .filter(o -> o.getEstadoOrden() != null && o.getEstadoOrden().getId().equals(estadoId))
                .filter(o -> {
                    if (empleado.getRol().getId() == 5L) {
                        return o.getItems().stream()
                                .noneMatch(i -> i.getProducto() != null && i.getProducto().getId() == 12);
                    }
                    return true;
                })
                .sorted(Comparator.comparing(Orden::getId).reversed())
                .collect(Collectors.toList());
    }

    private Comparator<Producto> crearComparadorProductos() {
        return (p1, p2) -> {
            String n1 = p1.getNombre();
            String n2 = p2.getNombre();
            String sinCat = "Sin categoria";

            if (n1.equalsIgnoreCase(sinCat)) return 1;  // n1 va al final
            if (n2.equalsIgnoreCase(sinCat)) return -1; // n2 va al final
            return n1.compareToIgnoreCase(n2);          // Orden A-Z normal
        };
    }
}