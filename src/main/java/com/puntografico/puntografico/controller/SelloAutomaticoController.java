package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SelloAutomaticoDTO;
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
public class SelloAutomaticoController {

    private final OpcionesSelloAutomaticoService opcionesSelloAutomaticoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final SelloAutomaticoService selloAutomaticoService;
    private final OrdenSelloAutomaticoService ordenSelloAutomaticoService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-sello-automatico", "/crear-odt-sello-automatico/{idOrden}"})
    public String verCrearOdtSelloAutomatico(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSelloAutomatico ordenSelloAutomatico = (idOrden != null) ? ordenSelloAutomaticoService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenSelloAutomatico != null) ? ordenSelloAutomatico.getOrdenTrabajo() : new OrdenTrabajo();
        SelloAutomatico selloAutomatico = (ordenSelloAutomatico != null) ? ordenSelloAutomatico.getSelloAutomatico() : new SelloAutomatico();

        List<ModeloSelloAutomatico> listaModeloSelloAutomatico = opcionesSelloAutomaticoService.buscarTodosModeloSelloAutomatico();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("selloAutomatico", selloAutomatico);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaModeloSelloAutomatico", listaModeloSelloAutomatico);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sello-automatico";
    }

    @GetMapping("/mostrar-odt-sello-automatico/{ordenSelloAutomaticoId}")
    public String verOrdenSelloAutomatico(@PathVariable("ordenSelloAutomaticoId") Long ordenSelloAutomaticoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSelloAutomatico ordenSelloAutomatico = ordenSelloAutomaticoService.buscarPorId(ordenSelloAutomaticoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenSelloAutomatico.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenSelloAutomatico.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenSelloAutomatico.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenSelloAutomatico", ordenSelloAutomatico);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-sello-automatico";
    }

    @PostMapping("/api/creacion-sello-automatico")
    public String creacionSelloAutomatico(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenSelloAutomatico ordenSelloAutomaticoExistente = (idOrden != null) ? ordenSelloAutomaticoService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenSelloAutomaticoExistente != null) ? ordenSelloAutomaticoExistente.getOrdenTrabajo().getId() : null;
        Long idSelloAutomatico = (ordenSelloAutomaticoExistente != null) ? ordenSelloAutomaticoExistente.getSelloAutomatico().getId() : null;
        Long idOrdenSelloAutomatico = (ordenSelloAutomaticoExistente != null) ? ordenSelloAutomaticoExistente.getId() : null;

        SelloAutomaticoDTO selloAutomaticoDTO = armarSelloAutomaticoDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        SelloAutomatico selloAutomatico = selloAutomaticoService.guardar(selloAutomaticoDTO, idSelloAutomatico);
        OrdenSelloAutomatico ordenSelloAutomatico = ordenSelloAutomaticoService.guardar(ordenTrabajo, selloAutomatico, idOrdenSelloAutomatico);

        return "redirect:/mostrar-odt-sello-automatico/" + ordenSelloAutomatico.getId();
    }

    private SelloAutomaticoDTO armarSelloAutomaticoDTO(HttpServletRequest request) {
        SelloAutomaticoDTO selloAutomaticoDTO = new SelloAutomaticoDTO();
        selloAutomaticoDTO.setEsProfesional(request.getParameter("esProfesional") != null);
        selloAutomaticoDTO.setEsParticular(request.getParameter("esParticular") != null);
        selloAutomaticoDTO.setTextoLineaUno(request.getParameter("textoLineaUno"));
        selloAutomaticoDTO.setTextoLineaDos(request.getParameter("textoLineaDos"));
        selloAutomaticoDTO.setTextoLineaTres(request.getParameter("textoLineaTres"));
        selloAutomaticoDTO.setTextoLineaCuatro(request.getParameter("textoLineaCuatro"));
        selloAutomaticoDTO.setTipografiaLineaUno(request.getParameter("tipografiaLineaUno"));
        selloAutomaticoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        selloAutomaticoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        selloAutomaticoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        selloAutomaticoDTO.setModeloSelloAutomaticoId(Long.parseLong(request.getParameter("modeloSelloAutomatico.id")));

        return selloAutomaticoDTO;
    }

    @DeleteMapping("/api/eliminar-orden-sello-automatico/{idOrden}")
    public void eliminarOrdenSelloAutomatico(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenSelloAutomatico ordenSelloAutomatico = ordenSelloAutomaticoService.buscarPorOrdenId(idOrden);
        Long idOrdenSelloAutomatico = ordenSelloAutomatico.getId();
        Long idOrdenTrabajo = ordenSelloAutomatico.getOrdenTrabajo().getId();
        Long idSelloAutomatico = ordenSelloAutomatico.getSelloAutomatico().getId();

        ordenSelloAutomaticoService.eliminar(idOrdenSelloAutomatico);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        selloAutomaticoService.eliminar(idSelloAutomatico);
    }
}
