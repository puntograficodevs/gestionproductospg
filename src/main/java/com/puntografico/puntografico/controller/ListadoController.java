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
import java.time.LocalTime;
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
                // 1. Filtrar por el estado (Pendiente, En Proceso, etc.)
                .filter(o -> o.getEstadoOrden() != null && o.getEstadoOrden().getId().equals(estadoId))

                // 2. Mantener la restricción del Rol 5 (no ve Producto ID 12)
                .filter(o -> {
                    if (empleado.getRol().getId() == 5L) {
                        return o.getItems().stream()
                                .noneMatch(i -> i.getProducto() != null && i.getProducto().getId() == 12);
                    }
                    return true;
                })

                // 3. Ordenar por Próximo a Vencer (Fecha DESC + Hora DESC)
                .sorted(Comparator.comparing(Orden::getFechaEntrega)
                        .thenComparing(o -> parsearHora(o.getHoraEntrega())))
                .collect(Collectors.toList());
    }

    /**
     * Normaliza los Strings de hora para que Java pueda compararlos.
     * Formatos aceptados: "11:30", "1130", "9:00", "9".
     */
    private LocalTime parsearHora(String horaStr) {
        if (horaStr == null || horaStr.trim().isEmpty()) {
            // Si no tiene hora, lo ponemos al final del día (23:59)
            // para que no tape a las que sí tienen horario definido.
            return LocalTime.MAX;
        }

        try {
            // Limpiamos puntos, dos puntos y espacios
            String limpia = horaStr.replace(":", "").replace(".", "").trim();

            int horas, minutos = 0;

            if (limpia.length() <= 2) {
                // Caso "9", "11", "20"
                horas = Integer.parseInt(limpia);
            } else if (limpia.length() == 3) {
                // Caso "930" -> 9:30
                horas = Integer.parseInt(limpia.substring(0, 1));
                minutos = Integer.parseInt(limpia.substring(1));
            } else {
                // Caso "1130" -> 11:30
                horas = Integer.parseInt(limpia.substring(0, 2));
                minutos = Integer.parseInt(limpia.substring(2));
            }

            // Validación básica de rangos
            return LocalTime.of(Math.min(horas, 23), Math.min(minutos, 59));

        } catch (Exception e) {
            // Ante cualquier error de formato, lo mandamos al final del día
            return LocalTime.MAX;
        }
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