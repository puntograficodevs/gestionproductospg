package com.puntografico.puntografico.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.OrdenService;
import com.puntografico.puntografico.service.PagoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ordenes")
@AllArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final MedioPagoService medioPagoService;
    private final PagoService pagoService;
    private final ProductoRepository productoRepository;
    private final OrdenRepository ordenRepository;

    @GetMapping("/nueva-orden")
    public String formulario(HttpSession session, Model model) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        model.addAttribute("empleado", empleado);
        model.addAttribute("orden", new Orden());
        model.addAttribute("productos", productoRepository.findAll());
        return "nueva-orden";
    }

    @PostMapping("/guardar-orden")
    public String guardar(@ModelAttribute("orden") Orden orden,
                          @RequestParam Map<String, String> allParams,
                          @RequestParam(value = "idMedioPago", required = false) Long idMedioPago) {

        Map<String, String> detallesMap = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (key.startsWith("detalles[") && value != null && !value.isEmpty()) {
                String cleanKey = key.replace("detalles[", "").replace("]", "");
                detallesMap.put(cleanKey, value);
            }
        });

        OrdenItem item = new OrdenItem();
        item.setCantidad(Integer.parseInt(allParams.getOrDefault("cantidadItem", "1")));

        if (allParams.containsKey("productoId")) {
            Producto p = new Producto();
            p.setId(Integer.parseInt(allParams.get("productoId")));
            item.setProducto(p);
        }

        try {
            item.setDetallePersonalizado(new ObjectMapper().writeValueAsString(detallesMap));
        } catch (JsonProcessingException e) {
            item.setDetallePersonalizado("{}");
        }

        item.setOrden(orden);
        if (orden.getItems() == null) orden.setItems(new ArrayList<>());
        orden.getItems().add(item);

        if (idMedioPago != null && orden.getAbonado() > 0) {
            pagoService.guardar(orden, idMedioPago);
        }

        ordenService.guardar(orden);

        System.out.println("Se llegó hasta este punto sin problema");
        return "redirect:/ordenes/exito/" + orden.getId();
    }

    @GetMapping("/formulario-producto/{id}")
    public String obtenerFragmento(@PathVariable Integer id, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de producto inválido:" + id));

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

        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();
        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("producto", producto);
        model.addAttribute("orden", new Orden());
        model.addAttribute("empleado", empleado);

        return "fragments/formulario-dinamico :: cuerpo-formulario";
    }

    @GetMapping("/exito/{id}")
    public String mostrarExito(HttpSession session, Model model, @PathVariable Long id) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        Orden orden = ordenService.buscarPorId(id);
        OrdenItem item = orden.getItems().get(0);
        Map<String, Object> detalles = new HashMap<>();
        try {
            detalles = new ObjectMapper().readValue(
                    item.getDetallePersonalizado(),
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            System.out.println("Hubo un error al mostrar la orden: " + e.getMessage());
        }

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", orden);
        model.addAttribute("detalles", detalles);
        model.addAttribute("item", item);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        model.addAttribute("fechaPedido", orden.getFechaPedido().format(formatter));
        model.addAttribute("fechaEntrega", orden.getFechaEntrega().format(formatter));
        if (orden.getFechaMuestra() != null) {
            model.addAttribute("fechaMuestra", orden.getFechaMuestra().format(formatter));
        }

        return "exito";
    }

    @GetMapping("/detalle-fragmento/{id}")
    public String obtenerFragmentoDetalle(@PathVariable Long id, Model model) {
        Orden orden = ordenRepository.findById(id).orElseThrow();
        model.addAttribute("orden", orden);

        return "exito :: detalle-orden";
    }
}
