package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.service.OrdenTrabajoService;
import com.puntografico.puntografico.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final OrdenTrabajoService ordenTrabajoService;

    @PostMapping("/api/pago/guardar")
    public String guardarPago(HttpServletRequest request) {
        Long idOrdenTrabajo = Long.parseLong(request.getParameter("idOrden"));
        pagoService.guardar(request, idOrdenTrabajo);
        ordenTrabajoService.actualizarTotalAbonado(request);

        return "redirect:/listado";
    }
}
