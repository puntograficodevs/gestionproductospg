package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Agenda;
import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.OrdenAgenda;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.dto.AgendaDTO;
import com.puntografico.puntografico.service.AgendaService;
import com.puntografico.puntografico.service.OrdenTrabajoService;
import com.puntografico.puntografico.service.PagoService;
import com.puntografico.puntografico.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller @AllArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final AgendaService agendaService;
    private final PagoService pagoService;

    @PostMapping("/api/creacion-producto")
    public String creacionProducto(HttpServletRequest request) {
        System.out.println("///////////////////////////// ESTA ENTRANDO AL ENDPOINT");
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));
        String tipoProducto = request.getParameter("tipoProducto");

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrden);
        System.out.println("Se guardó la orden de trabajo " + ordenTrabajo.getId());
        pagoService.guardar(request, ordenTrabajo.getId());
        crearProductoCorrespondiente(request, tipoProducto, ordenTrabajo.getId());

        return "redirect:/mostrar-odt-producto/" + ordenTrabajo.getId();
    }

    private void crearProductoCorrespondiente(HttpServletRequest request, String tipoProducto, Long idOrden) {
        Assert.notNull(tipoProducto, "El tipo de producto no puede ser nulo.");

        switch (tipoProducto) {
            case "agenda":
                AgendaDTO agendaDTO = armarAgendaDTO(request);
                agendaService.guardar(agendaDTO, idOrden);
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }
    }

    private AgendaDTO armarAgendaDTO(HttpServletRequest request) {
        System.out.println("//////////////////////");
        System.out.println("la medida es " + request.getParameter("medida"));
        AgendaDTO agendaDTO = new AgendaDTO();
        agendaDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        agendaDTO.setTipoTapaAgendaId(Long.parseLong(request.getParameter("tipoTapaAgenda.id")));
        agendaDTO.setTipoColorAgendaId(Long.parseLong(request.getParameter("tipoColorAgenda.id")));
        agendaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        agendaDTO.setMedida(request.getParameter("medida"));
        agendaDTO.setTipoTapaPersonalizada(request.getParameter("tipoTapaPersonalizada"));
        agendaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        agendaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        agendaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));

        return agendaDTO;
    }

    @GetMapping("/mostrar-odt-producto/{ordenTrabajoId}")
    public String verOrdenProducto(@PathVariable("ordenTrabajoId") Long ordenTrabajoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        String htmlRedireccion;

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.buscarPorId(ordenTrabajoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenTrabajo.getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenTrabajo.getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenTrabajo.getFechaPedido());
        String tipoProducto = ordenTrabajo.getTipoProducto();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        switch(tipoProducto) {
            case "agenda":
                Agenda agenda = agendaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Agenda no encontrada para la orden"));
                model.addAttribute("agenda", agenda);
                htmlRedireccion = "mostrar-odt-agenda";
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }

        return htmlRedireccion;
    }

    @DeleteMapping("/api/eliminar-producto/{idOrden}")
    @ResponseBody
    public void eliminarProducto(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoService.buscarPorId(idOrden);
        String tipoProducto = ordenTrabajo.getTipoProducto();

        switch (tipoProducto) {
            case "agenda":
                agendaService.eliminar(ordenTrabajo.getId());
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }

        ordenTrabajoService.eliminar(ordenTrabajo.getId());
    }
}
