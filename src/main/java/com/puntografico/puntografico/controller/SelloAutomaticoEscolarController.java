package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SelloAutomaticoEscolarDTO;
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
public class SelloAutomaticoEscolarController {

    private final OpcionesSelloAutomaticoEscolarService opcionesSelloAutomaticoEscolarService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final SelloAutomaticoEscolarService selloAutomaticoEscolarService;
    private final OrdenSelloAutomaticoEscolarService ordenSelloAutomaticoEscolarService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-sello-automatico-escolar", "/crear-odt-sello-automatico-escolar/{idOrden}"})
    public String verCrearOdtSelloAutomaticoEscolar(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolar = (idOrden != null) ? ordenSelloAutomaticoEscolarService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenSelloAutomaticoEscolar != null) ? ordenSelloAutomaticoEscolar.getOrdenTrabajo() : new OrdenTrabajo();
        SelloAutomaticoEscolar selloAutomaticoEscolar = (ordenSelloAutomaticoEscolar != null) ? ordenSelloAutomaticoEscolar.getSelloAutomaticoEscolar() : new SelloAutomaticoEscolar();

        List<ModeloSelloAutomaticoEscolar> listaModeloSelloAutomaticoEscolar = opcionesSelloAutomaticoEscolarService.buscarTodosModeloSelloAutomaticoEscolar();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("selloAutomaticoEscolar", selloAutomaticoEscolar);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaModeloSelloAutomaticoEscolar", listaModeloSelloAutomaticoEscolar);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sello-automatico-escolar";
    }

    @GetMapping("/mostrar-odt-sello-automatico-escolar/{ordenSelloAutomaticoEscolarId}")
    public String verOrdenSelloAutomaticoEscolar(@PathVariable("ordenSelloAutomaticoEscolarId") Long ordenSelloAutomaticoEscolarId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolar = ordenSelloAutomaticoEscolarService.buscarPorId(ordenSelloAutomaticoEscolarId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenSelloAutomaticoEscolar.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenSelloAutomaticoEscolar.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenSelloAutomaticoEscolar.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenSelloAutomaticoEscolar", ordenSelloAutomaticoEscolar);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-sello-automatico-escolar";
    }

    @PostMapping("/api/creacion-sello-automatico-escolar")
    public String creacionSelloAutomaticoEscolar(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolarExistente = (idOrden != null) ? ordenSelloAutomaticoEscolarService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenSelloAutomaticoEscolarExistente != null) ? ordenSelloAutomaticoEscolarExistente.getOrdenTrabajo().getId() : null;
        Long idSelloAutomaticoEscolar = (ordenSelloAutomaticoEscolarExistente != null) ? ordenSelloAutomaticoEscolarExistente.getSelloAutomaticoEscolar().getId() : null;
        Long idOrdenSelloAutomaticoEscolar = (ordenSelloAutomaticoEscolarExistente != null) ? ordenSelloAutomaticoEscolarExistente.getId() : null;

        SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO = armarSelloAutomaticoEscolarDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        SelloAutomaticoEscolar selloAutomaticoEscolar = selloAutomaticoEscolarService.guardar(selloAutomaticoEscolarDTO, idSelloAutomaticoEscolar);
        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolar = ordenSelloAutomaticoEscolarService.guardar(ordenTrabajo, selloAutomaticoEscolar, idOrdenSelloAutomaticoEscolar);

        return "redirect:/mostrar-odt-sello-automatico-escolar/" + ordenSelloAutomaticoEscolar.getId();
    }

    private SelloAutomaticoEscolarDTO armarSelloAutomaticoEscolarDTO(HttpServletRequest request) {
        SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO = new SelloAutomaticoEscolarDTO();
        selloAutomaticoEscolarDTO.setTextoLineaUno(request.getParameter("textoLineaUno"));
        selloAutomaticoEscolarDTO.setTextoLineaDos(request.getParameter("textoLineaDos"));
        selloAutomaticoEscolarDTO.setTextoLineaTres(request.getParameter("textoLineaTres"));
        selloAutomaticoEscolarDTO.setTipografia(request.getParameter("tipografia"));
        selloAutomaticoEscolarDTO.setDibujo(request.getParameter("dibujo"));
        selloAutomaticoEscolarDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        selloAutomaticoEscolarDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        selloAutomaticoEscolarDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        selloAutomaticoEscolarDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        selloAutomaticoEscolarDTO.setModeloSelloAutomaticoEscolarId(Long.parseLong(request.getParameter("modeloSelloAutomaticoEscolar.id")));

        return selloAutomaticoEscolarDTO;
    }

    @DeleteMapping("/api/eliminar-orden-sello-automatico-escolar/{idOrden}")
    public void eliminarOrdenSelloAutomaticoEscolar(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolar = ordenSelloAutomaticoEscolarService.buscarPorOrdenId(idOrden);
        Long idOrdenSelloAutomaticoEscolar = ordenSelloAutomaticoEscolar.getId();
        Long idOrdenTrabajo = ordenSelloAutomaticoEscolar.getOrdenTrabajo().getId();
        Long idSelloAutomaticoEscolar = ordenSelloAutomaticoEscolar.getSelloAutomaticoEscolar().getId();

        ordenSelloAutomaticoEscolarService.eliminar(idOrdenSelloAutomaticoEscolar);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        selloAutomaticoEscolarService.eliminar(idSelloAutomaticoEscolar);
    }
}
