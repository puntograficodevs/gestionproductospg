package com.puntografico.puntografico.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.repository.ProductoRepository;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.OrdenService;
import com.puntografico.puntografico.service.PagoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    public String guardar(@ModelAttribute("orden") Orden orden, @RequestParam(value = "idMedioPago", required = false) Long idMedioPago) {
        if (idMedioPago != null) {
            orden = pagoService.guardar(orden, idMedioPago);
        }

        ordenService.guardar(orden);
        return "redirect:/exito";
    }

    @GetMapping("/formulario-producto/{id}")
    public String obtenerFragmento(@PathVariable Integer id, Model model) {
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
        model.addAttribute("orden", new Orden()); // Objeto vacío para el th:object

        return "fragments/formulario-dinamico :: cuerpo-formulario";
    }
}
