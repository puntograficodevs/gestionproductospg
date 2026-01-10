package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TarjetaDTO;
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
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final MedioPagoService medioPagoService;
    private final OpcionesTarjetaService opcionesTarjetaService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenTarjetaService ordenTarjetaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-tarjeta", "/crear-odt-tarjeta/{idOrden}"})
    public String verCrearOdtTarjeta(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenTarjeta ordenTarjeta = (idOrden != null) ? ordenTarjetaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenTarjeta != null) ? ordenTarjeta.getOrdenTrabajo() : new OrdenTrabajo();
        Tarjeta tarjeta = (ordenTarjeta != null) ? ordenTarjeta.getTarjeta() : new Tarjeta();

        List<TipoPapelTarjeta> listaTipoPapelTarjeta = opcionesTarjetaService.buscarTodosTipoPapelTarjeta();
        List<TipoColorTarjeta> listaTipoColorTarjeta = opcionesTarjetaService.buscarTodosTipoColorTarjeta();
        List<TipoFazTarjeta> listaTipoFazTarjeta = opcionesTarjetaService.buscarTodosTipoFazTarjeta();
        List<TipoLaminadoTarjeta> listaTipoLaminadoTarjeta = opcionesTarjetaService.buscarTodosTipoLaminadoTarjeta();
        List<MedidaTarjeta> listaMedidaTarjeta = opcionesTarjetaService.buscarTodosMedidaTarjeta();
        List<CantidadTarjeta> listaCantidadTarjeta = opcionesTarjetaService.buscarTodosCantidadTarjeta();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("tarjeta", tarjeta);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelTarjeta", listaTipoPapelTarjeta);
        model.addAttribute("listaTipoColorTarjeta", listaTipoColorTarjeta);
        model.addAttribute("listaTipoFazTarjeta", listaTipoFazTarjeta);
        model.addAttribute("listaTipoLaminadoTarjeta", listaTipoLaminadoTarjeta);
        model.addAttribute("listaMedidaTarjeta", listaMedidaTarjeta);
        model.addAttribute("listaCantidadTarjeta", listaCantidadTarjeta);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-tarjeta";
    }

    @GetMapping("/mostrar-odt-tarjeta/{ordenTarjetaId}")
    public String verOrdenTarjeta(@PathVariable("ordenTarjetaId") Long ordenTarjetaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTarjeta ordenTarjeta = ordenTarjetaService.buscarPorId(ordenTarjetaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenTarjeta.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenTarjeta.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenTarjeta.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTarjeta", ordenTarjeta);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-tarjeta";
    }

    @PostMapping("/api/creacion-tarjeta")
    public String creacionTarjeta(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenTarjeta ordenTarjetaExistente = (idOrden != null) ? ordenTarjetaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenTarjetaExistente != null) ? ordenTarjetaExistente.getOrdenTrabajo().getId() : null;
        Long idTarjeta = (ordenTarjetaExistente != null) ? ordenTarjetaExistente.getTarjeta().getId() : null;
        Long idOrdenTarjeta = (ordenTarjetaExistente != null) ? ordenTarjetaExistente.getId() : null;

        TarjetaDTO tarjetaDTO = armarTarjetaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Tarjeta tarjeta = tarjetaService.guardar(tarjetaDTO, idTarjeta);
        OrdenTarjeta ordenTarjeta = ordenTarjetaService.guardar(ordenTrabajo, tarjeta, idOrdenTarjeta);

        return "redirect:/mostrar-odt-tarjeta/" + ordenTarjeta.getId();
    }

    private TarjetaDTO armarTarjetaDTO(HttpServletRequest request) {
        TarjetaDTO tarjetaDTO = new TarjetaDTO();
        tarjetaDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        tarjetaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        tarjetaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        tarjetaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        tarjetaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        tarjetaDTO.setTipoPapelTarjetaId(Long.parseLong(request.getParameter("tipoPapelTarjeta.id")));
        tarjetaDTO.setTipoColorTarjetaId(Long.parseLong(request.getParameter("tipoColorTarjeta.id")));
        tarjetaDTO.setTipoFazTarjetaId(Long.parseLong(request.getParameter("tipoFazTarjeta.id")));
        tarjetaDTO.setTipoLaminadoTarjetaId(Long.parseLong(request.getParameter("tipoLaminadoTarjeta.id")));
        tarjetaDTO.setMedidaTarjetaId(Long.parseLong(request.getParameter("medidaTarjeta.id")));
        tarjetaDTO.setCantidadTarjetaId(Long.parseLong(request.getParameter("cantidadTarjeta.id")));

        return tarjetaDTO;
    }

    @DeleteMapping("/api/eliminar-orden-tarjeta/{idOrden}")
    public void eliminarOrdenTarjeta(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenTarjeta ordenTarjeta = ordenTarjetaService.buscarPorOrdenId(idOrden);
        Long idOrdenTarjeta = ordenTarjeta.getId();
        Long idOrdenTrabajo = ordenTarjeta.getOrdenTrabajo().getId();
        Long idTarjeta = ordenTarjeta.getTarjeta().getId();

        ordenTarjetaService.eliminar(idOrdenTarjeta);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        tarjetaService.eliminar(idTarjeta);
    }
}
