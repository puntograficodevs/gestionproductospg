package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.AnotadorDTO;
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
public class AnotadorController {

    private final MedioPagoService medioPagoService;
    private final OrdenAnotadorService ordenAnotadorService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final AnotadorService anotadorService;
    private final ProductoService productoService;
    private final PagoService pagoService;


    @GetMapping({"/crear-odt-anotador", "/crear-odt-anotador/{idOrden}"})
    public String verCrearOdtAnotador(HttpSession session, Model model, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenAnotador ordenAnotador = (idOrden != null) ? ordenAnotadorService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenAnotador != null) ? ordenAnotador.getOrdenTrabajo() : new OrdenTrabajo();
        Anotador anotador = (ordenAnotador != null) ? ordenAnotador.getAnotador() : new Anotador();

        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("anotador", anotador);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-anotador";
    }

    @GetMapping("/mostrar-odt-anotador/{ordenAnotadorId}")
    public String verOrdenAnotador(@PathVariable("ordenAnotadorId") Long ordenAnotadorId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenAnotador ordenAnotador = ordenAnotadorService.buscarPorId(ordenAnotadorId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenAnotador.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenAnotador.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenAnotador.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenAnotador", ordenAnotador);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-anotador";
    }

    @PostMapping("/api/creacion-anotador")
    public String creacionAnotador(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenAnotador ordenAnotadorExistente = (idOrden != null) ? ordenAnotadorService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenAnotadorExistente != null) ? ordenAnotadorExistente.getOrdenTrabajo().getId() : null;
        Long idAnotador = (ordenAnotadorExistente != null) ? ordenAnotadorExistente.getAnotador().getId() : null;
        Long idOrdenAnotador = (ordenAnotadorExistente != null) ? ordenAnotadorExistente.getId() : null;

        AnotadorDTO anotadorDTO = armarAnotadorDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Anotador anotador = anotadorService.guardar(anotadorDTO, idAnotador);
        OrdenAnotador ordenAnotador = ordenAnotadorService.guardar(ordenTrabajo, anotador, idOrdenAnotador);

        return "redirect:/mostrar-odt-anotador/" + ordenAnotador.getId();
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

    @DeleteMapping("/api/eliminar-orden-anotador/{idOrden}")
    public void eliminarOrdenAnotador(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenAnotador ordenAnotador = ordenAnotadorService.buscarPorOrdenId(idOrden);
        Long idOrdenAnotador = ordenAnotador.getId();
        Long idOrdenTrabajo = ordenAnotador.getOrdenTrabajo().getId();
        Long idAnotador = ordenAnotador.getAnotador().getId();

        ordenAnotadorService.eliminar(idOrdenAnotador);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        anotadorService.eliminar(idAnotador);
    }
}
