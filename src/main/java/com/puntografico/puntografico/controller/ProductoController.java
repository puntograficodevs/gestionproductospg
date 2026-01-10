package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Agenda;
import com.puntografico.puntografico.domain.Anotador;
import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.dto.AgendaDTO;
import com.puntografico.puntografico.dto.AnotadorDTO;
import com.puntografico.puntografico.service.*;
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
    private final AnotadorService anotadorService;
    private final PagoService pagoService;

    @PostMapping("/api/creacion-producto")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));
        String tipoProducto = request.getParameter("tipoProducto");

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrden);
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
            case "anotador":
                AnotadorDTO anotadorDTO = armarAnotadorDTO(request);
                anotadorService.guardar(anotadorDTO, idOrden);
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }
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
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("agenda", agenda);
                htmlRedireccion = "mostrar-odt-agenda";
                break;
            case "anotador":
                Anotador anotador = anotadorService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("anotador", anotador);
                htmlRedireccion = "mostrar-odt-anotador";
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
            case "anotador":
                anotadorService.eliminar(ordenTrabajo.getId());
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }

        ordenTrabajoService.eliminar(ordenTrabajo.getId());
    }

    private AgendaDTO armarAgendaDTO(HttpServletRequest request) {
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

    private AnotadorDTO armarAnotadorDTO(HttpServletRequest request) {
        AnotadorDTO anotadorDTO = new AnotadorDTO();
        anotadorDTO.setMedida(request.getParameter("medida"));
        anotadorDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        anotadorDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        anotadorDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        anotadorDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        anotadorDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        anotadorDTO.setTipoTapa(request.getParameter("tipoTapa"));

        return anotadorDTO;
    }
}
