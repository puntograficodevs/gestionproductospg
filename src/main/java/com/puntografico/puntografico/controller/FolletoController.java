package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.FolletoDTO;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class FolletoController {

    private final OpcionesFolletoService opcionesFolletoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final FolletoService folletoService;
    private final OrdenFolletoService ordenFolletoService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-folleto", "/crear-odt-folleto/{idOrden}"})
    public String verCrearOdtFolleto(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenFolleto ordenFolleto = (idOrden != null) ? ordenFolletoService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenFolleto != null) ? ordenFolleto.getOrdenTrabajo() : new OrdenTrabajo();
        Folleto folleto = (ordenFolleto != null) ? ordenFolleto.getFolleto() : new Folleto();

        List<TipoPapelFolleto> listaTipoPapelFolleto = opcionesFolletoService.buscarTodosTipoPapelFolleto();
        List<TipoColorFolleto> listaTipoColorFolleto = opcionesFolletoService.buscarTodosTipoColorFolleto();
        List<TipoFazFolleto> listaTipoFazFolleto = opcionesFolletoService.buscarTodosTipoFazFolleto();
        List<TamanioHojaFolleto> listaTamanioHojaFolleto = opcionesFolletoService.buscarTodosTamanioHojaFolleto();
        List<TipoFolleto> listaTipoFolleto = opcionesFolletoService.buscarTodosTipoFolleto();
        List<CantidadFolleto> listaCantidadFolleto = opcionesFolletoService.buscarTodosCantidadFolleto();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("folleto", folleto);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelFolleto", listaTipoPapelFolleto);
        model.addAttribute("listaTipoColorFolleto", listaTipoColorFolleto);
        model.addAttribute("listaTipoFazFolleto", listaTipoFazFolleto);
        model.addAttribute("listaTamanioHojaFolleto", listaTamanioHojaFolleto);
        model.addAttribute("listaTipoFolleto", listaTipoFolleto);
        model.addAttribute("listaCantidadFolleto", listaCantidadFolleto);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-folleto";
    }

    @GetMapping("/mostrar-odt-folleto/{ordenFolletoId}")
    public String verOrdenFolleto(@PathVariable("ordenFolletoId") Long ordenFolletoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenFolleto ordenFolleto = ordenFolletoService.buscarPorId(ordenFolletoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenFolleto.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenFolleto.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenFolleto.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenFolleto", ordenFolleto);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-folleto";
    }

    @PostMapping("/api/creacion-folleto")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenFolleto ordenFolletoExistente = (idOrden != null) ? ordenFolletoService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenFolletoExistente != null) ? ordenFolletoExistente.getOrdenTrabajo().getId() : null;
        Long idFolleto = (ordenFolletoExistente != null) ? ordenFolletoExistente.getFolleto().getId() : null;
        Long idOrdenFolleto = (ordenFolletoExistente != null) ? ordenFolletoExistente.getId() : null;

        FolletoDTO folletoDTO = armarFolletoDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Folleto folleto = folletoService.guardar(folletoDTO, idFolleto);
        OrdenFolleto ordenFolleto = ordenFolletoService.guardar(ordenTrabajo, folleto, idOrdenFolleto);

        return "redirect:/mostrar-odt-folleto/" + ordenFolleto.getId();
    }

    private FolletoDTO armarFolletoDTO(HttpServletRequest request) {
        FolletoDTO folletoDTO = new FolletoDTO();
        folletoDTO.setConPlegado(request.getParameter("conPlegado") != null);
        folletoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        folletoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        folletoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        folletoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        folletoDTO.setTipoPapelFolletoId(Long.parseLong(request.getParameter("tipoPapelFolleto.id")));
        folletoDTO.setTipoColorFolletoId(Long.parseLong(request.getParameter("tipoColorFolleto.id")));
        folletoDTO.setTipoFazFolletoId(Long.parseLong(request.getParameter("tipoFazFolleto.id")));
        folletoDTO.setTamanioHojaFolletoId(Long.parseLong(request.getParameter("tamanioHojaFolleto.id")));
        folletoDTO.setTipoFolletoId(Long.parseLong(request.getParameter("tipoFolleto.id")));
        folletoDTO.setCantidadFolletoId(Long.parseLong(request.getParameter("cantidadFolleto.id")));

        return folletoDTO;
    }

    @DeleteMapping("/api/eliminar-orden-folleto/{idOrden}")
    public void eliminarOrdenFolleto(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenFolleto ordenFolleto = ordenFolletoService.buscarPorOrdenId(idOrden);
        Long idOrdenFolleto = ordenFolleto.getId();
        Long idOrdenTrabajo = ordenFolleto.getOrdenTrabajo().getId();
        Long idFolleto = ordenFolleto.getFolleto().getId();

        ordenFolletoService.eliminar(idOrdenFolleto);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        folletoService.eliminar(idFolleto);
    }
}
