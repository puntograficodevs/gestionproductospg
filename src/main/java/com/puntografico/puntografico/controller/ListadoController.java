package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ListadoController {

    private final OrdenService ordenService;
    private final MedioPagoService medioPagoService;
    private final ProductoService productoService;

    @GetMapping("/listado")
    public String verListado(@RequestParam(value = "producto", required = false) String productoFiltro, HttpSession session, Model model) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        List<Orden> ordenesSinHacer = ordenService.buscarOrdenesConEstadoSegunRol(1L, empleado.getRol().getId());
        List<Orden> ordenesEnProceso = ordenService.buscarOrdenesConEstadoSegunRol(2L, empleado.getRol().getId());
        List<Orden> ordenesListaParaRetirar = ordenService.buscarOrdenesConEstadoSegunRol(3L, empleado.getRol().getId());
        List<Orden> ordenesCorregir = ordenService.buscarOrdenesConEstadoSegunRol(4L, empleado.getRol().getId());
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();
        List<Producto> productos = productoService.buscarTodos().stream()
                .sorted(crearComparadorProductos())
                .collect(Collectors.toList());

        model.addAttribute("filtroSeleccionado", productoFiltro != null ? productoFiltro : "todas");
        model.addAttribute("ordenesSinHacer", ordenService.ordenarYFormatear(ordenesSinHacer, empleado));
        model.addAttribute("ordenesEnProceso", ordenService.ordenarYFormatear(ordenesEnProceso, empleado));
        model.addAttribute("ordenesListaParaRetirar", ordenService.ordenarYFormatear(ordenesListaParaRetirar, empleado));
        model.addAttribute("ordenesCorregir", ordenService.ordenarYFormatear(ordenesCorregir, empleado));
        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("productos", productos);
        model.addAttribute("empleado", empleado);

        return "listado";
    }



    private Comparator<Producto> crearComparadorProductos() {
        return (productoUno, productoDos) -> {
            String nombreUno = productoUno.getNombre();
            String nombreDos = productoDos.getNombre();
            String sinCategoria = "Sin categoria";

            if (nombreUno.equalsIgnoreCase(sinCategoria)) return 1;
            if (nombreDos.equalsIgnoreCase(sinCategoria)) return -1;
            return nombreUno.compareToIgnoreCase(nombreDos);
        };
    }
}