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
        List<Orden> ordenesSinHacer = ordenService.buscarOrdenesEficientesParaListado(1L, empleado.getRol().getId());
        List<Orden> ordenesEnProceso = ordenService.buscarOrdenesEficientesParaListado(2L, empleado.getRol().getId());
        List<Orden> ordenesListaParaRetirar = ordenService.buscarOrdenesEficientesParaListado(3L, empleado.getRol().getId());
        List<Orden> ordenesCorregir = ordenService.buscarOrdenesEficientesParaListado(4L, empleado.getRol().getId());
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();
        List<Producto> productos = productoService.buscarTodos().stream()
                .sorted(crearComparadorProductos())
                .collect(Collectors.toList());

        model.addAttribute("filtroSeleccionado", productoFiltro != null ? productoFiltro : "todas");
        model.addAttribute("ordenesSinHacer", ordenarYFormatear(ordenesSinHacer, empleado));
        model.addAttribute("ordenesEnProceso", ordenarYFormatear(ordenesEnProceso, empleado));
        model.addAttribute("ordenesListaParaRetirar", ordenarYFormatear(ordenesListaParaRetirar, empleado));
        model.addAttribute("ordenesCorregir", ordenarYFormatear(ordenesCorregir, empleado));
        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("productos", productos);
        model.addAttribute("empleado", empleado);

        return "listado";
    }

    private List<Orden> ordenarYFormatear(List<Orden> ordenes, Empleado empleado) {
        return ordenes.stream()
                .sorted(obtenerCriterioOrdenamiento(empleado))
                .collect(Collectors.toList());
    }

    private Comparator<Orden> obtenerCriterioOrdenamiento(Empleado empleado) {
        if ("maricommunity".equalsIgnoreCase(empleado.getUsername())) {
            return Comparator.comparing(Orden::getId).reversed();
        }

        return Comparator.comparing(Orden::getFechaEntrega)
                .thenComparing(orden -> parsearHora(orden.getHoraEntrega()));
    }

    private LocalTime parsearHora(String horaEntrega) {
        if (esHoraEntregaInvalida(horaEntrega)) {
            return LocalTime.MAX;
        }

        try {
            String horaEntregaFormateada = formatearHoraEntrega(horaEntrega);
            return construirLocalTime(horaEntregaFormateada);
        } catch (Exception e) {
            return LocalTime.MAX;
        }
    }

    private boolean esHoraEntregaInvalida(String hora) {
        return hora == null || hora.trim().isEmpty();
    }

    private String formatearHoraEntrega(String horaEntrega) {
        return horaEntrega.replace(":", "").replace(".", "").trim();
    }

    private LocalTime construirLocalTime(String horaEntregaFormateada) {
        int horas;
        int minutos = 0;

        if (horaEntregaFormateada.length() <= 2) {
            horas = Integer.parseInt(horaEntregaFormateada);
        } else if (horaEntregaFormateada.length() == 3) {
            horas = Integer.parseInt(horaEntregaFormateada.substring(0, 1));
            minutos = Integer.parseInt(horaEntregaFormateada.substring(1));
        } else {
            horas = Integer.parseInt(horaEntregaFormateada.substring(0, 2));
            minutos = Integer.parseInt(horaEntregaFormateada.substring(2));
        }

        return LocalTime.of(normalizarHora(horas), normalizarMinutos(minutos));
    }

    private int normalizarHora(int horas) {
        return Math.min(horas, 23);
    }

    private int normalizarMinutos(int minutos) {
        return Math.min(minutos, 59);
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