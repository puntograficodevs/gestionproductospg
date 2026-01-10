package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ViniloDTO;
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
public class ViniloController {

    private final OpcionesViniloService opcionesViniloService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ViniloService viniloService;
    private final OrdenViniloService ordenViniloService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-vinilo", "/crear-odt-vinilo/{idOrden}"})
    public String verCrearOdtVinilo(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenVinilo ordenVinilo = (idOrden != null) ? ordenViniloService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenVinilo != null) ? ordenVinilo.getOrdenTrabajo() : new OrdenTrabajo();
        Vinilo vinilo = (ordenVinilo != null) ? ordenVinilo.getVinilo() : new Vinilo();

        List<TipoVinilo> listaTipoVinilo = opcionesViniloService.buscarTodosTipoVinilo();
        List<TipoAdicionalVinilo> listaTipoAdicionalVinilo = opcionesViniloService.buscarTodosTipoAdicionalVinilo();
        List<TipoCorteVinilo> listaTipoCorteVinilo = opcionesViniloService.buscarTodosTipoCorteVinilo();
        List<MedidaVinilo> listaMedidaVinilo = opcionesViniloService.buscarTodosMedidaVinilo();
        List<CantidadVinilo> listaCantidadVinilo = opcionesViniloService.buscarTodosCantidadVinilo();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("vinilo", vinilo);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoVinilo", listaTipoVinilo);
        model.addAttribute("listaTipoAdicionalVinilo", listaTipoAdicionalVinilo);
        model.addAttribute("listaTipoCorteVinilo", listaTipoCorteVinilo);
        model.addAttribute("listaMedidaVinilo", listaMedidaVinilo);
        model.addAttribute("listaCantidadVinilo", listaCantidadVinilo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-vinilo";
    }

    @GetMapping("/mostrar-odt-vinilo/{ordenViniloId}")
    public String verOrdenVinilo(@PathVariable("ordenViniloId") Long ordenViniloId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenVinilo ordenVinilo = ordenViniloService.buscarPorId(ordenViniloId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenVinilo.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenVinilo.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenVinilo.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenVinilo", ordenVinilo);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-vinilo";
    }

    @PostMapping("/api/creacion-vinilo")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenVinilo ordenViniloExistente = (idOrden != null) ? ordenViniloService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenViniloExistente != null) ? ordenViniloExistente.getOrdenTrabajo().getId() : null;
        Long idVinilo = (ordenViniloExistente != null) ? ordenViniloExistente.getVinilo().getId() : null;
        Long idOrdenVinilo = (ordenViniloExistente != null) ? ordenViniloExistente.getId() : null;

        ViniloDTO viniloDTO = armarViniloDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Vinilo vinilo = viniloService.guardar(viniloDTO, idVinilo);
        OrdenVinilo ordenVinilo = ordenViniloService.guardar(ordenTrabajo, vinilo, idOrdenVinilo);

        return "redirect:/mostrar-odt-vinilo/" + ordenVinilo.getId();
    }

    private ViniloDTO armarViniloDTO(HttpServletRequest request) {
        ViniloDTO viniloDTO = new ViniloDTO();
        viniloDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        viniloDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        viniloDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        viniloDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        viniloDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        viniloDTO.setTipoViniloId(Long.parseLong(request.getParameter("tipoVinilo.id")));
        viniloDTO.setTipoAdicionalViniloId(Long.parseLong(request.getParameter("tipoAdicionalVinilo.id")));
        viniloDTO.setTipoCorteViniloId(Long.parseLong(request.getParameter("tipoCorteVinilo.id")));
        viniloDTO.setMedidaViniloId(Long.parseLong(request.getParameter("medidaVinilo.id")));
        viniloDTO.setCantidadViniloId(Long.parseLong(request.getParameter("cantidadVinilo.id")));

        return viniloDTO;
    }

    @DeleteMapping("/api/eliminar-orden-vinilo/{idOrden}")
    public void eliminarOrdenVinilo(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenVinilo ordenVinilo = ordenViniloService.buscarPorOrdenId(idOrden);
        Long idOrdenVinilo = ordenVinilo.getId();
        Long idOrdenTrabajo = ordenVinilo.getOrdenTrabajo().getId();
        Long idVinilo = ordenVinilo.getVinilo().getId();

        ordenViniloService.eliminar(idOrdenVinilo);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        viniloService.eliminar(idVinilo);
    }
}
