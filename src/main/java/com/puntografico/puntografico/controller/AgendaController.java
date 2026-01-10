package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.AgendaDTO;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller @AllArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;
    private final MedioPagoService medioPagoService;
    private final OpcionesAgendaService opcionesAgendaService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenAgendaService ordenAgendaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-agenda", "/crear-odt-agenda/{idOrden}"})
    public String verCrearOdtAgenda(
            HttpSession session,
            Model model,
            @PathVariable(required = false) Long idOrden) {

        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenAgenda ordenAgenda = (idOrden != null) ? ordenAgendaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenAgenda != null) ? ordenAgenda.getOrdenTrabajo() : new OrdenTrabajo();
        Agenda agenda = (ordenAgenda != null)? ordenAgenda.getAgenda() : new Agenda();

        List<TipoTapaAgenda> listaTipoTapaAgenda = opcionesAgendaService.buscarTodosTipoTapaAgenda();
        List<TipoColorAgenda> listaTipoColorAgenda = opcionesAgendaService.buscarTodosTipoColorAgenda();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("agenda", agenda);
        model.addAttribute("listaTipoTapaAgenda", listaTipoTapaAgenda);
        model.addAttribute("listaTipoColorAgenda", listaTipoColorAgenda);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-agenda";
    }

    @GetMapping("/mostrar-odt-agenda/{ordenAgendaId}")
    public String verOrdenAgenda(@PathVariable("ordenAgendaId") Long ordenAgendaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenAgenda ordenAgenda = ordenAgendaService.buscarPorId(ordenAgendaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenAgenda.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenAgenda.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenAgenda.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenAgenda", ordenAgenda);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-agenda";
    }

    @PostMapping("/api/creacion-agenda")
    public String creacionAgenda(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenAgenda ordenAgendaExistente = (idOrden != null) ? ordenAgendaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenAgendaExistente != null) ? ordenAgendaExistente.getOrdenTrabajo().getId() : null;
        Long idAgenda = (ordenAgendaExistente != null) ? ordenAgendaExistente.getAgenda().getId() : null;
        Long idOrdenAgenda = (ordenAgendaExistente != null) ? ordenAgendaExistente.getId() : null;

        AgendaDTO agendaDTO = armarAgendaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Agenda agenda = agendaService.guardar(agendaDTO, idAgenda);
        OrdenAgenda ordenAgenda = ordenAgendaService.guardar(ordenTrabajo, agenda, idOrdenAgenda);

        return "redirect:/mostrar-odt-agenda/" + ordenAgenda.getId();
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

    @DeleteMapping("/api/eliminar-orden-agenda/{idOrden}")
    public void eliminarOrdenAgenda(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenAgenda ordenAgenda = ordenAgendaService.buscarPorOrdenId(idOrden);
        Long idOrdenTrabajo = ordenAgenda.getOrdenTrabajo().getId();
        Long idAgenda = ordenAgenda.getAgenda().getId();
        Long idOrdenAgenda = ordenAgenda.getId();

        ordenAgendaService.eliminar(idOrdenAgenda);
        ordenTrabajoService.eliminar(ordenAgenda.getOrdenTrabajo().getId());
        agendaService.eliminar(ordenAgenda.getAgenda().getId());
    }
}
