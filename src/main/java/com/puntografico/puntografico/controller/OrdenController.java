package com.puntografico.puntografico.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
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
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final EstadoPagoRepository estadoPagoRepository;

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

        // 1. Extraer detalles dinámicos del Map global
        Map<String, String> detallesMap = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (key.startsWith("detalles[") && value != null && !value.isEmpty()) {
                String cleanKey = key.replace("detalles[", "").replace("]", "");
                detallesMap.put(cleanKey, value);
            }
        });

        OrdenItem item;
        if (orden.getId() != null) {
            // --- CASO EDICIÓN ---
            Orden ordenExistente = ordenService.buscarPorId(orden.getId());

            // Recuperamos el ítem existente (asumiendo uno por orden)
            item = ordenExistente.getItems().get(0);

            // Seteamos en el objeto 'orden' que viene del form los datos que no están en el HTML
            // para que el Service no los pise con null o valores por defecto
            orden.setFechaPedido(ordenExistente.getFechaPedido());
            orden.setEstadoOrden(ordenExistente.getEstadoOrden());
            orden.setPagos(ordenExistente.getPagos()); // Clave para no borrar señas previas

            // Vinculamos el ítem a la orden del modelo
            if (orden.getItems() == null) orden.setItems(new ArrayList<>());
            orden.getItems().add(item);
        } else {
            // --- CASO NUEVA ORDEN ---
            item = new OrdenItem();
            item.setOrden(orden);
            if (orden.getItems() == null) orden.setItems(new ArrayList<>());
            orden.getItems().add(item);
        }

        // 2. Actualizar datos del Ítem (común a ambos casos)
        item.setCantidad(Integer.parseInt(allParams.getOrDefault("cantidadItem", "1")));

        if (allParams.containsKey("productoId")) {
            Producto p = new Producto();
            p.setId(Integer.parseInt(allParams.get("productoId")));
            item.setProducto(p);
        }

        // Serializar el Map de detalles a JSON String
        try {
            item.setDetallePersonalizado(new ObjectMapper().writeValueAsString(detallesMap));
        } catch (JsonProcessingException e) {
            item.setDetallePersonalizado("{}");
        }

        // 3. Procesar el Subtotal (el campo que agregamos al dominio)
        if (allParams.containsKey("subtotal")) {
            // Lo parseamos a double y redondeamos para evitar problemas de coma
            double subtotalExtraido = Double.parseDouble(allParams.get("subtotal"));
            orden.setSubtotal((int) Math.ceil(subtotalExtraido));
        }

        if (allParams.containsKey("precioDisenio")) {
            try {
                double disenioExtraido = Double.parseDouble(allParams.get("precioDisenio"));
                orden.setPrecioDisenio((int) Math.ceil(disenioExtraido));
            } catch (NumberFormatException e) {
                orden.setPrecioDisenio(0);
            }
        }

        // 4. Registrar nuevo pago solo si hay monto y medio seleccionado
        // (pagoService.guardar internamente debería manejar el .add() a la lista de pagos)
        if (idMedioPago != null && orden.getAbonado() > 0) {
            pagoService.guardar(orden, idMedioPago);
        }

        // 5. Guardado final mediante el Service
        ordenService.guardar(orden);

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

    @GetMapping("/editar-orden/{id}")
    public String editarOrden(@PathVariable Long id, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        if (empleado == null) return "redirect:/";

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
        model.addAttribute("empleado", empleado);
        model.addAttribute("listaMediosDePago", medioPagoService.buscarTodos());
        model.addAttribute("esEdicion", true);

        return "nueva-orden";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Long id) {
        ordenService.eliminar(id); // El service debe llamar a repository.deleteById(id)
        return "redirect:/buscador"; // O a la ruta de tu buscador
    }

    @PostMapping("/eliminar-varias")
    public String eliminarVarias(@RequestParam("ids") String ids) {
        if (ids != null && !ids.isEmpty()) {
            String[] arrayIds = ids.split(",");
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

    @GetMapping("/pasar-en-proceso/{id}")
    public String pasarEnProceso(@PathVariable Long id) {
        cambiarEstado(id, 2L); // Supongamos que ID 2 es "En Proceso"
        return "redirect:/listado";
    }

    @GetMapping("/volver-sin-hacer/{id}")
    public String volverSinHacer(@PathVariable Long id) {
        cambiarEstado(id, 1L); // Supongamos que ID 1 es "Sin Hacer"
        return "redirect:/listado";
    }

    @GetMapping("/pasar-lista-para-retirar/{id}")
    public String pasarAListaParaRetirar(@PathVariable Long id) {
        cambiarEstado(id, 3L); // Supongamos que ID 3 es "Lista para retirar"
        return "redirect:/listado";
    }

    @GetMapping("/pasar-retirada/{id}")
    public String pasarRetirada(@PathVariable Long id) {
        cambiarEstado(id, 5L); // Supongamos que ID 4 es "Entregada"
        return "redirect:/listado";
    }

    private void cambiarEstado(Long ordenId, Long estadoId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + ordenId));

        EstadoOrden nuevoEstado = estadoOrdenRepository.findById(estadoId)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + estadoId));

        orden.setEstadoOrden(nuevoEstado);
        ordenRepository.save(orden);
    }

    @PostMapping("/enviar-a-corregir")
    public String enviarACorregir(@RequestParam("id") Long id,
                                  @RequestParam("motivo") String motivo) {

        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // 1. Guardamos el estado que tiene ahora (antes de pasar a 4)
        // Usamos el ID del objeto EstadoOrden que ya tiene la orden
        orden.setIdEstadoPrevio(orden.getEstadoOrden().getId().intValue());

        // 2. Le seteamos el nuevo estado (4 = Corregir)
        EstadoOrden estadoCorregir = estadoOrdenRepository.findById(4L).get();
        orden.setEstadoOrden(estadoCorregir);

        // 3. Guardamos el texto de la corrección
        orden.setCorreccion(motivo);

        ordenRepository.save(orden);

        return "redirect:/listado";
    }

    @PostMapping("/registrar-pago")
    public String registrarPago(@RequestParam("ordenId") Long ordenId,
                                @RequestParam("importe") Integer importe,
                                @RequestParam("idMedioPago") Long idMedioPago) {

        pagoService.registrarPagoExtra(ordenId, importe, idMedioPago);

        return "redirect:/listado";
    }
}
