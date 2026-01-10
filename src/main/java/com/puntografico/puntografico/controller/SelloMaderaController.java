package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SelloMaderaDTO;
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
public class SelloMaderaController {

    private final OpcionesSelloMaderaService opcionesSelloMaderaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final SelloMaderaService selloMaderaService;
    private final OrdenSelloMaderaService ordenSelloMaderaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-sello-madera", "/crear-odt-sello-madera/{idOrden}"})
    public String verCrearOdtSelloMadera(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSelloMadera ordenSelloMadera = (idOrden != null) ? ordenSelloMaderaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenSelloMadera != null) ? ordenSelloMadera.getOrdenTrabajo() : new OrdenTrabajo();
        SelloMadera selloMadera = (ordenSelloMadera != null) ? ordenSelloMadera.getSelloMadera() : new SelloMadera();

        List<TamanioSelloMadera> listaTamanioSelloMadera = opcionesSelloMaderaService.buscarTodosTamanioSelloMadera();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("selloMadera", selloMadera);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTamanioSelloMadera", listaTamanioSelloMadera);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sello-madera";
    }

    @GetMapping("/mostrar-odt-sello-madera/{ordenSelloMaderaId}")
    public String verOrdenSelloMadera(@PathVariable("ordenSelloMaderaId") Long ordenSelloMaderaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSelloMadera ordenSelloMadera = ordenSelloMaderaService.buscarPorId(ordenSelloMaderaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenSelloMadera.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenSelloMadera.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenSelloMadera.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenSelloMadera", ordenSelloMadera);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-sello-madera";
    }

    @PostMapping("/api/creacion-sello-madera")
    public String creacionSelloMadera(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenSelloMadera ordenSelloMaderaExistente = (idOrden != null) ? ordenSelloMaderaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenSelloMaderaExistente != null) ? ordenSelloMaderaExistente.getOrdenTrabajo().getId() : null;
        Long idSelloMadera = (ordenSelloMaderaExistente != null) ? ordenSelloMaderaExistente.getSelloMadera().getId() : null;
        Long idOrdenSelloMadera = (ordenSelloMaderaExistente != null) ? ordenSelloMaderaExistente.getId() : null;

        SelloMaderaDTO selloMaderaDTO = armarSelloMaderaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        SelloMadera selloMadera = selloMaderaService.guardar(selloMaderaDTO, idSelloMadera);
        OrdenSelloMadera ordenSelloMadera = ordenSelloMaderaService.guardar(ordenTrabajo, selloMadera, idOrdenSelloMadera);

        return "redirect:/mostrar-odt-sello-madera/" + ordenSelloMadera.getId();
    }

    private SelloMaderaDTO armarSelloMaderaDTO(HttpServletRequest request) {
        SelloMaderaDTO selloMaderaDTO = new SelloMaderaDTO();
        selloMaderaDTO.setTamanioPersonalizado(request.getParameter("tamanioPersonalizado"));
        selloMaderaDTO.setConAdicionalPerilla(request.getParameter("conAdicionalPerilla") != null);
        selloMaderaDTO.setDetalleSello(request.getParameter("detalleSello"));
        selloMaderaDTO.setTipografiaLineaUno(request.getParameter("tipografiaLineaUno"));
        selloMaderaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        selloMaderaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        selloMaderaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        selloMaderaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        selloMaderaDTO.setTamanioSelloMaderaId(Long.parseLong(request.getParameter("tamanioSelloMadera.id")));

        return selloMaderaDTO;
    }

    @DeleteMapping("/api/eliminar-orden-sello-madera/{idOrden}")
    public void eliminarOrdenSelloMadera(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenSelloMadera ordenSelloMadera = ordenSelloMaderaService.buscarPorOrdenId(idOrden);
        Long idOrdenSelloMadera = ordenSelloMadera.getId();
        Long idOrdenTrabajo = ordenSelloMadera.getOrdenTrabajo().getId();
        Long idSelloMadera = ordenSelloMadera.getSelloMadera().getId();

        ordenSelloMaderaService.eliminar(idOrdenSelloMadera);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        selloMaderaService.eliminar(idSelloMadera);
    }
}
