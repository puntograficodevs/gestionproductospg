package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class ViniloPlasticoCorrugadoController {

    private final OpcionesViniloPlasticoCorrugadoService opcionesViniloPlasticoCorrugadoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ViniloPlasticoCorrugadoService viniloPlasticoCorrugadoService;

    @GetMapping({"/crear-odts/crear-odt-vinilo-plastico-corrugado", "/crear-odts/crear-odt-vinilo-plastico-corrugado/{idOrden}"})
    public String verCrearOdtViniloPlasticoCorrugado(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        ViniloPlasticoCorrugado viniloPlasticoCorrugado = viniloPlasticoCorrugadoService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(ViniloPlasticoCorrugado::new);

        List<MedidaViniloPlasticoCorrugado> listaMedidaViniloPlasticoCorrugado = opcionesViniloPlasticoCorrugadoService.buscarTodosMedidaViniloPlasticoCorrugado();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("viniloPlasticoCorrugado", viniloPlasticoCorrugado);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaViniloPlasticoCorrugado", listaMedidaViniloPlasticoCorrugado);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odts/crear-odt-vinilo-plastico-corrugado";
    }
}
