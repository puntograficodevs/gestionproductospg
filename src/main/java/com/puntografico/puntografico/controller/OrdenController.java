package com.puntografico.puntografico.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ordenes")
@AllArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final MedioPagoService medioPagoService;
    private final PagoService pagoService;
    private final ProductoService productoService;
    private final ProductoCatalogoService productoCatalogoService;

    @GetMapping("/nueva-orden")
    public String formulario(HttpSession session, Model model) {
        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));
        model.addAttribute("orden", new Orden());
        model.addAttribute("productos", productoService.traerTodosLosProductosOrdenadosPorNombre());
        return "nueva-orden";
    }

    @PostMapping("/guardar-orden")
    public String guardarOrden(@ModelAttribute Orden orden,
                               @RequestParam("productoId") Integer idProducto,
                               @RequestParam(required = false) Long idMedioPago) {
        try {
            ObjectMapper mapperDeItems = new ObjectMapper();
            orden.getItems().removeIf(item -> item.getDetalles() == null || item.getDetalles().isEmpty());

            for (OrdenItem item : orden.getItems()) {
                item.setDetallePersonalizado(mapperDeItems.writeValueAsString(item.getDetalles()));
            }

            Orden ordenGuardada = ordenService.guardar(orden, idProducto, idMedioPago);

            return "redirect:/ordenes/exito/" + ordenGuardada.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/ordenes/error";
        }
    }

    @GetMapping("/formulario-producto/{id}")
    public String obtenerFragmento(
            @PathVariable Integer id,
            @RequestParam(value = "index", defaultValue = "0") int index, // <--- Recibimos el índice
            Model model,
            HttpSession session) {

        Producto producto = productoService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID de producto inválido:" + id));
        List<ProductoCatalogo> listaMateriales = traerListaMaterialesOrdenada();
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Map<String, Object>> esquema = mapper.readValue(
                    producto.getEsquemaConfiguracion(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            model.addAttribute("campos", esquema);
        } catch (Exception e) {
            model.addAttribute("campos", new ArrayList<>());
        }

        model.addAttribute("index", index); // <--- Lo pasamos a la vista
        model.addAttribute("listaMediosDePago", medioPagoService.buscarTodos());
        model.addAttribute("listaMateriales", listaMateriales);
        model.addAttribute("producto", producto);
        model.addAttribute("orden", new Orden());
        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));

        return (index == 0)
                ? "fragments/formulario-dinamico :: cuerpo-formulario"
                : "fragments/formulario-dinamico :: bloque-item-card";
    }

    private List<ProductoCatalogo> traerListaMaterialesOrdenada() {
        List<ProductoCatalogo> listaMateriales = productoCatalogoService.buscarTodasLasCopiasEscolaresEnCatalogo();
        listaMateriales.sort((materialUno, materialDos) ->
                materialUno.getNombreNegocio().compareToIgnoreCase(materialDos.getNombreNegocio())
        );

        return listaMateriales;
    }

    @GetMapping("/exito/{id}")
    public String mostrarExito(HttpSession session, Model model, @PathVariable Long id) {
        Orden orden = ordenService.buscarPorId(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));
        model.addAttribute("ordenTrabajo", orden);
        model.addAttribute("fechaPedido", orden.getFechaPedido().format(formatter));
        model.addAttribute("fechaEntrega", orden.getFechaEntrega().format(formatter));

        if (orden.getFechaMuestra() != null) {
            model.addAttribute("fechaMuestra", orden.getFechaMuestra().format(formatter));
        }

        return "exito";
    }

    @GetMapping("/detalle-fragmento/{id}")
    public String obtenerFragmentoDetalle(@PathVariable Long id, Model model) {
        Orden orden = ordenService.buscarPorId(id);
        model.addAttribute("orden", orden);
        model.addAttribute("listaMediosDePago", medioPagoService.buscarTodos());

        return "exito :: detalle-orden";
    }

    @GetMapping("/editar-orden/{id}")
    public String editarOrden(@PathVariable Long id, Model model, HttpSession session) {
        Orden orden = ordenService.buscarPorId(id);
        Producto producto = orden.getItems().get(0).getProducto();
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<Map<String, Object>> esquema = mapper.readValue(
                    producto.getEsquemaConfiguracion(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            model.addAttribute("campos", esquema);
        } catch (Exception e) {
            model.addAttribute("campos", new ArrayList<>());
        }

        model.addAttribute("orden", orden);
        model.addAttribute("producto", producto);
        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));
        model.addAttribute("listaMediosDePago", medioPagoService.buscarTodos());
        model.addAttribute("listaMateriales", productoCatalogoService.buscarTodasLasCopiasEscolaresEnCatalogo());
        model.addAttribute("esEdicion", true);

        return "nueva-orden";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Long id) {
        ordenService.eliminar(id);
        return "redirect:/buscador";
    }

    @PostMapping("/eliminar-varias")
    public String eliminarVarias(@RequestParam("ids") String idsAEliminar) {
        if (idsAEliminar != null && !idsAEliminar.isEmpty()) {
            String[] arrayIds = idsAEliminar.split(",");
            for (String idStr : arrayIds) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    ordenService.eliminar(id);
                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear ID: " + idStr);
                }
            }
        }
        return "redirect:/buscador";
    }

    @GetMapping("/pasar-retirada-desde-buscador/{id}")
    public String pasarRetiradaDesdeBuscador(@PathVariable Long id) {
        ordenService.cambiarEstadoOrden(id, 5L);
        return "redirect:/buscador";
    }

    @GetMapping("/cambiar-estado/{idOrden}")
    public String cambiarEstado(@PathVariable Long idOrden, @RequestParam(value = "producto", defaultValue="todas") String producto, @RequestParam("nuevoEstado") Long idNuevoEstado) {
        ordenService.cambiarEstadoOrden(idOrden, idNuevoEstado);
        return "redirect:/listado?producto=" + producto;
    }

    @PostMapping("/enviar-a-corregir")
    public String enviarACorregir(@RequestParam("id") Long idOrden,
                                  @RequestParam("motivo") String motivo) {
        ordenService.enviarAColumnaCorreccion(idOrden, motivo);

        return "redirect:/listado";
    }

    @PostMapping("/registrar-pago")
    public String registrarPago(@RequestParam("ordenId") Long ordenId,
                                @RequestParam("importe") Integer importe,
                                @RequestParam("idMedioPago") Long idMedioPago,
                                @RequestParam(value = "desdeModal", defaultValue = "false") boolean desdeModal) {

        pagoService.registrarPagoExtra(ordenId, importe, idMedioPago);

        if (desdeModal) {
            return "redirect:/buscador";
        } else {
            return "redirect:/listado";
        }
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(
            @RequestParam(defaultValue = "entrega") String tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lunes,
            Model model, HttpSession session) {

        if (lunes == null) {
            lunes = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }

        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        List<Orden> ordenesSemana = ordenService.obtenerOrdenesPorSemana(lunes, tipo, empleado);

        Map<LocalDate, List<Orden>> datosCalendario = ordenesSemana.stream()
                .collect(Collectors.groupingBy(
                        orden -> "muestra".equals(tipo) ? orden.getFechaMuestra() : orden.getFechaEntrega(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        model.addAttribute("datos", datosCalendario);
        model.addAttribute("lunesActual", lunes);
        model.addAttribute("tipoActual", tipo);
        model.addAttribute("semanaPrevia", lunes.minusWeeks(1));
        model.addAttribute("semanaSiguiente", lunes.plusWeeks(1));
        model.addAttribute("empleado", empleado);

        return "calendario";
    }
}
