package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SublimacionDTO;
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
public class SublimacionController {

    private final SublimacionService sublimacionService;
    private final MedioPagoService medioPagoService;
    private final OpcionesSublimacionService opcionesSublimacionService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenSublimacionService ordenSublimacionService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-sublimacion", "/crear-odt-sublimacion/{idOrden}"})
    public String verCrearOdtSublimacion(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSublimacion ordenSublimacion = (idOrden != null) ? ordenSublimacionService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenSublimacion != null) ? ordenSublimacion.getOrdenTrabajo() : new OrdenTrabajo();
        Sublimacion sublimacion = (ordenSublimacion != null) ? ordenSublimacion.getSublimacion() : new Sublimacion();

        List<MaterialSublimacion> listaMaterialSublimacion = opcionesSublimacionService.buscarTodosMaterialSublimacion();
        List<CantidadSublimacion> listaCantidadSublimacion = opcionesSublimacionService.buscarTodosCantidadSublimacion();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("sublimacion", sublimacion);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMaterialSublimacion", listaMaterialSublimacion);
        model.addAttribute("listaCantidadSublimacion", listaCantidadSublimacion);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sublimacion";
    }

    @GetMapping("/mostrar-odt-sublimacion/{ordenSublimacionId}")
    public String verOrdenSublimacion(@PathVariable("ordenSublimacionId") Long ordenSublimacionId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSublimacion ordenSublimacion = ordenSublimacionService.buscarPorId(ordenSublimacionId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenSublimacion.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenSublimacion.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenSublimacion.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenSublimacion", ordenSublimacion);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-sublimacion";
    }

    @PostMapping("/api/creacion-sublimacion")
    public String creacionSublimacion(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenSublimacion ordenSublimacionExistente = (idOrden != null) ? ordenSublimacionService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenSublimacionExistente != null) ? ordenSublimacionExistente.getOrdenTrabajo().getId() : null;
        Long idSublimacion = (ordenSublimacionExistente != null) ? ordenSublimacionExistente.getSublimacion().getId() : null;
        Long idOrdensublimacion = (ordenSublimacionExistente != null) ? ordenSublimacionExistente.getId() : null;

        SublimacionDTO sublimacionDTO = armarSublimacionDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Sublimacion sublimacion = sublimacionService.guardar(sublimacionDTO, idSublimacion);
        OrdenSublimacion ordenSublimacion = ordenSublimacionService.guardar(ordenTrabajo, sublimacion, idOrdensublimacion);

        return "redirect:/mostrar-odt-sublimacion/" + ordenSublimacion.getId();
    }

    private SublimacionDTO armarSublimacionDTO(HttpServletRequest request) {
        SublimacionDTO sublimacionDTO = new SublimacionDTO();
        sublimacionDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        sublimacionDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        sublimacionDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        sublimacionDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        sublimacionDTO.setMaterialSublimacionId(Long.parseLong(request.getParameter("materialSublimacion.id")));
        sublimacionDTO.setCantidadSublimacionId(Long.parseLong(request.getParameter("cantidadSublimacion.id")));

        return sublimacionDTO;
    }

    @DeleteMapping("/api/eliminar-orden-sublimacion/{idOrden}")
    public void eliminarOrdenSublimacion(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenSublimacion ordenSublimacion = ordenSublimacionService.buscarPorOrdenId(idOrden);
        Long idOrdenSublimacion = ordenSublimacion.getId();
        Long idOrdenTrabajo = ordenSublimacion.getOrdenTrabajo().getId();
        Long idSublimacion = ordenSublimacion.getSublimacion().getId();

        ordenSublimacionService.eliminar(idOrdenSublimacion);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        sublimacionService.eliminar(idSublimacion);
    }
}
