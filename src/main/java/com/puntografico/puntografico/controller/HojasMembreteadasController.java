package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.HojasMembreteadasDTO;
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
public class HojasMembreteadasController {

    private final OpcionesHojasMembreteadasService opcionesHojasMembreteadasService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final HojasMembreteadasService hojasMembreteadasService;
    private final OrdenHojasMembreteadasService ordenHojasMembreteadasService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-hojas-membreteadas", "/crear-odt-hojas-membreteadas/{idOrden}"})
    public String verCreadOdtHojasMembreteadas(Model model,
                                               HttpSession session,
                                               @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenHojasMembreteadas ordenHojasMembreteadas = (idOrden != null) ? ordenHojasMembreteadasService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenHojasMembreteadas != null) ? ordenHojasMembreteadas.getOrdenTrabajo() : new OrdenTrabajo();
        HojasMembreteadas hojasMembreteadas = (ordenHojasMembreteadas != null) ? ordenHojasMembreteadas.getHojasMembreteadas() : new HojasMembreteadas();

        List<MedidaHojasMembreteadas> listaMedidaHojasMembreteadas = opcionesHojasMembreteadasService.buscarTodosMedidaHojasMembreteadas();
        List<TipoColorHojasMembreteadas> listaTipoColorHojasMembreteadas = opcionesHojasMembreteadasService.buscarTodosTipoColorHojasMembreteadas();
        List<CantidadHojasMembreteadas> listaCantidadHojasMembreteadas = opcionesHojasMembreteadasService.buscarTodosCantidadHojasMembreteadas();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("hojasMembreteadas", hojasMembreteadas);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaHojasMembreteadas", listaMedidaHojasMembreteadas);
        model.addAttribute("listaTipoColorHojasMembreteadas", listaTipoColorHojasMembreteadas);
        model.addAttribute("listaCantidadHojasMembreteadas", listaCantidadHojasMembreteadas);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-hojas-membreteadas";
    }

    @GetMapping("/mostrar-odt-hojas-membreteadas/{ordenHojasMembreteadasId}")
    public String verOrdenHojasMembreteadas(@PathVariable("ordenHojasMembreteadasId") Long ordenHojasMembreteadasId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenHojasMembreteadas ordenHojasMembreteadas = ordenHojasMembreteadasService.buscarPorId(ordenHojasMembreteadasId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenHojasMembreteadas.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenHojasMembreteadas.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenHojasMembreteadas.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenHojasMembreteadas", ordenHojasMembreteadas);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-hojas-membreteadas";
    }

    @PostMapping("/api/creacion-hojas-membreteadas")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenHojasMembreteadas ordenHojasMembreteadasExistente = (idOrden != null) ? ordenHojasMembreteadasService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenHojasMembreteadasExistente != null) ? ordenHojasMembreteadasExistente.getOrdenTrabajo().getId() : null;
        Long idHojasMembreteadas = (ordenHojasMembreteadasExistente != null) ? ordenHojasMembreteadasExistente.getHojasMembreteadas().getId() : null;
        Long idOrdenHojasMembreteadas = (ordenHojasMembreteadasExistente != null) ? ordenHojasMembreteadasExistente.getId() : null;

        HojasMembreteadasDTO hojasMembreteadasDTO = armarHojasMembreteadasDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        HojasMembreteadas hojasMembreteadas = hojasMembreteadasService.guardar(hojasMembreteadasDTO, idHojasMembreteadas);
        OrdenHojasMembreteadas ordenHojasMembreteadas = ordenHojasMembreteadasService.guardar(ordenTrabajo, hojasMembreteadas, idOrdenHojasMembreteadas);

        return "redirect:/mostrar-odt-hojas-membreteadas/" + ordenHojasMembreteadas.getId();
    }

    private HojasMembreteadasDTO armarHojasMembreteadasDTO(HttpServletRequest request) {
        HojasMembreteadasDTO hojasMembreteadasDTO = new HojasMembreteadasDTO();
        hojasMembreteadasDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        hojasMembreteadasDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        hojasMembreteadasDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        hojasMembreteadasDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        hojasMembreteadasDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        hojasMembreteadasDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        hojasMembreteadasDTO.setMedidaHojasMembreteadasId(Long.parseLong(request.getParameter("medidaHojasMembreteadas.id")));
        hojasMembreteadasDTO.setTipoColorHojasMembreteadasId(Long.parseLong(request.getParameter("tipoColorHojasMembreteadas.id")));
        hojasMembreteadasDTO.setCantidadHojasMembreteadasId(Long.parseLong(request.getParameter("cantidadHojasMembreteadas.id")));

        return hojasMembreteadasDTO;
    }

    @DeleteMapping("/api/eliminar-orden-hojas-membreteadas/{idOrden}")
    public void eliminarOrdenHojasMembreteadas(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenHojasMembreteadas ordenHojasMembreteadas = ordenHojasMembreteadasService.buscarPorOrdenId(idOrden);
        Long idOrdenHojasMembreteadas = ordenHojasMembreteadas.getId();
        Long idOrdenTrabajo = ordenHojasMembreteadas.getOrdenTrabajo().getId();
        Long idHojasMembreteadas = ordenHojasMembreteadas.getHojasMembreteadas().getId();

        ordenHojasMembreteadasService.eliminar(idOrdenHojasMembreteadas);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        hojasMembreteadasService.eliminar(idHojasMembreteadas);
    }
}
