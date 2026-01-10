package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TalonarioDTO;
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
public class TalonarioController {

    private final TalonarioService talonarioService;
    private final MedioPagoService medioPagoService;
    private final OpcionesTalonarioService opcionesTalonarioService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenTalonarioService ordenTalonarioService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-talonario", "/crear-odt-talonario/{idOrden}"})
    public String verCrearOdtTalonario(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTalonario ordenTalonario = (idOrden != null) ? ordenTalonarioService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenTalonario != null) ? ordenTalonario.getOrdenTrabajo() : new OrdenTrabajo();
        Talonario talonario = (ordenTalonario != null) ? ordenTalonario.getTalonario() : new Talonario();

        List<TipoTalonario> listaTipoTalonario = opcionesTalonarioService.buscarTodosTipoTalonario();
        List<TipoTroqueladoTalonario> listaTipoTroqueladoTalonario = opcionesTalonarioService.buscarTodosTipoTroqueladoTalonario();
        List<ModoTalonario> listaModoTalonario = opcionesTalonarioService.buscarTodosModoTalonario();
        List<TipoColorTalonario> listaTipoColorTalonario = opcionesTalonarioService.buscarTodosTipoColorTalonario();
        List<MedidaTalonario> listaMedidaTalonario = opcionesTalonarioService.buscarTodosMedidaTalonario();
        List<TipoPapelTalonario> listaTipoPapelTalonario = opcionesTalonarioService.buscarTodosTipoPapelTalonario();
        List<CantidadTalonario> listaCantidadTalonario = opcionesTalonarioService.buscarTodosCantidadTalonario();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("talonario", talonario);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoTalonario", listaTipoTalonario);
        model.addAttribute("listaTipoTroqueladoTalonario", listaTipoTroqueladoTalonario);
        model.addAttribute("listaModoTalonario", listaModoTalonario);
        model.addAttribute("listaTipoColorTalonario", listaTipoColorTalonario);
        model.addAttribute("listaMedidaTalonario", listaMedidaTalonario);
        model.addAttribute("listaTipoPapelTalonario", listaTipoPapelTalonario);
        model.addAttribute("listaCantidadTalonario", listaCantidadTalonario);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-talonario";
    }

    @GetMapping("/mostrar-odt-talonario/{ordenTalonarioId}")
    public String verOrdenTalonario(@PathVariable("ordenTalonarioId") Long ordenTalonarioId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTalonario ordenTalonario = ordenTalonarioService.buscarPorId(ordenTalonarioId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenTalonario.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenTalonario.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenTalonario.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTalonario", ordenTalonario);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-talonario";
    }

    @PostMapping("/api/creacion-talonario")
    public String creacionTalonario(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenTalonario ordenTalonarioExistente = (idOrden != null) ? ordenTalonarioService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenTalonarioExistente != null) ? ordenTalonarioExistente.getOrdenTrabajo().getId() : null;
        Long idTalonario = (ordenTalonarioExistente != null) ? ordenTalonarioExistente.getTalonario().getId() : null;
        Long idOrdenTalonario = (ordenTalonarioExistente != null) ? ordenTalonarioExistente.getId() : null;

        TalonarioDTO talonarioDTO = armarTalonarioDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Talonario talonario = talonarioService.guardar(talonarioDTO, idTalonario);
        OrdenTalonario ordenTalonario = ordenTalonarioService.guardar(ordenTrabajo, talonario, idOrdenTalonario);

        return "redirect:/mostrar-odt-talonario/" + ordenTalonario.getId();
    }

    private TalonarioDTO armarTalonarioDTO(HttpServletRequest request) {
        TalonarioDTO talonarioDTO = new TalonarioDTO();
        talonarioDTO.setConNumerado(request.getParameter("conNumerado") != null);
        talonarioDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        talonarioDTO.setDetalleNumerado(request.getParameter("detalleNumerado"));
        talonarioDTO.setEsEncolado(request.getParameter("esEncolado") != null);
        talonarioDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        talonarioDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        talonarioDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        talonarioDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        talonarioDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        talonarioDTO.setTipoTalonarioId(Long.parseLong(request.getParameter("tipoTalonario.id")));
        talonarioDTO.setTipoTroqueladoTalonarioId(Long.parseLong(request.getParameter("tipoTroqueladoTalonario.id")));
        talonarioDTO.setModoTalonarioId(Long.parseLong(request.getParameter("modoTalonario.id")));
        talonarioDTO.setTipoColorTalonarioId(Long.parseLong(request.getParameter("tipoColorTalonario.id")));
        talonarioDTO.setMedidaTalonarioId(Long.parseLong(request.getParameter("medidaTalonario.id")));
        talonarioDTO.setTipoPapelTalonarioId(Long.parseLong(request.getParameter("tipoPapelTalonario.id")));
        talonarioDTO.setCantidadTalonarioId(Long.parseLong(request.getParameter("cantidadTalonario.id")));

        return talonarioDTO;
    }

    @DeleteMapping("/api/eliminar-orden-talonario/{idOrden}")
    public void eliminarOrdenTalonario(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenTalonario ordenTalonario = ordenTalonarioService.buscarPorOrdenId(idOrden);
        Long idOrdenTalonario = ordenTalonario.getId();
        Long idOrdenTrabajo = ordenTalonario.getOrdenTrabajo().getId();
        Long idTalonario = ordenTalonario.getTalonario().getId();

        ordenTalonarioService.eliminar(idOrdenTalonario);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        talonarioService.eliminar(idTalonario);
    }
}
