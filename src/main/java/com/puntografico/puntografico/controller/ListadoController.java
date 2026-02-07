package com.puntografico.puntografico.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ListadoController {
/*
    private final OrdenTrabajoService ordenTrabajoService;
    private final MedioPagoService medioPagoService;

    @GetMapping("/listado")
    public String listado(HttpSession session, Model model, @RequestParam(required = false) String tipoProducto) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        if (tipoProducto == null) {
            tipoProducto = "todas";
        }

        List<OrdenTrabajo> ordenesSinHacer = ordenTrabajoService.buscarEstadoSinHacer(empleado, tipoProducto);
        List<OrdenTrabajo> ordenesCorregir = ordenTrabajoService.buscarEstadoCorregir(empleado, tipoProducto);
        List<OrdenTrabajo> ordenesEnProceso = ordenTrabajoService.buscarEstadoEnProceso(empleado, tipoProducto);
        List<OrdenTrabajo> ordenesListaParaRetirar = ordenTrabajoService.buscarEstadoListaParaRetirar(empleado, tipoProducto);
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenesSinHacer", ordenesSinHacer);
        model.addAttribute("ordenesCorregir", ordenesCorregir);
        model.addAttribute("ordenesEnProceso", ordenesEnProceso);
        model.addAttribute("ordenesListaParaRetirar", ordenesListaParaRetirar);
        model.addAttribute("tipoProducto", tipoProducto);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "listado";
    }*/
}
