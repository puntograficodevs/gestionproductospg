package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SobreDTO;
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
public class SobreController {

    private final SobreService sobreService;
    private final MedioPagoService medioPagoService;
    private final OpcionesSobreService opcionesSobreService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenSobreService ordenSobreService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-sobre", "/crear-odt-sobre/{idOrden}"})
    public String verCrearOdtSobre(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSobre ordenSobre = (idOrden != null) ? ordenSobreService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenSobre != null) ? ordenSobre.getOrdenTrabajo() : new OrdenTrabajo();
        Sobre sobre = (ordenSobre != null) ? ordenSobre.getSobre() : new Sobre();

        List<MedidaSobre> listaMedidaSobre = opcionesSobreService.buscarTodosMedidaSobre();
        List<TipoColorSobre> listaTipoColorSobre = opcionesSobreService.buscarTodosTipoColorSobre();
        List<CantidadSobre> listaCantidadSobre = opcionesSobreService.buscarTodosCantidadSobre();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("sobre", sobre);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaSobre", listaMedidaSobre);
        model.addAttribute("listaTipoColorSobre", listaTipoColorSobre);
        model.addAttribute("listaCantidadSobre", listaCantidadSobre);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sobre";
    }

    @GetMapping("/mostrar-odt-sobre/{ordenSobreId}")
    public String verOrdenSobre(@PathVariable("ordenSobreId") Long ordenSobreId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSobre ordenSobre = ordenSobreService.buscarPorId(ordenSobreId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenSobre.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenSobre.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenSobre.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenSobre", ordenSobre);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-sobre";
    }

    @PostMapping("/api/creacion-sobre")
    public String creacionSobre(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenSobre ordenSobreExistente = (idOrden != null) ? ordenSobreService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenSobreExistente != null) ? ordenSobreExistente.getOrdenTrabajo().getId() : null;
        Long idSobre = (ordenSobreExistente != null) ? ordenSobreExistente.getSobre().getId() : null;
        Long idOrdenSobre = (ordenSobreExistente != null) ? ordenSobreExistente.getId() : null;

        SobreDTO sobreDTO = armarSobreDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Sobre sobre = sobreService.guardar(sobreDTO, idSobre);
        OrdenSobre ordenSobre = ordenSobreService.guardar(ordenTrabajo, sobre, idOrdenSobre);

        return "redirect:/mostrar-odt-sobre/" + ordenSobre.getId();
    }

    private SobreDTO armarSobreDTO(HttpServletRequest request) {
        SobreDTO sobreDTO = new SobreDTO();
        sobreDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        sobreDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        sobreDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        sobreDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        sobreDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        sobreDTO.setMedidaSobreId(Long.parseLong(request.getParameter("medidaSobre.id")));
        sobreDTO.setTipoColorSobreId(Long.parseLong(request.getParameter("tipoColorSobre.id")));
        sobreDTO.setCantidadSobreId(Long.parseLong(request.getParameter("cantidadSobre.id")));

        return sobreDTO;
    }

    @DeleteMapping("/api/eliminar-orden-sobre/{idOrden}")
    public void eliminarOrdenSobre(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenSobre ordenSobre = ordenSobreService.buscarPorOrdenId(idOrden);
        Long idOrdenSobre = ordenSobre.getId();
        Long idOrdenTrabajo = ordenSobre.getOrdenTrabajo().getId();
        Long idSobre = ordenSobre.getSobre().getId();

        ordenSobreService.eliminar(idOrdenSobre);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        sobreService.eliminar(idSobre);
    }
}
