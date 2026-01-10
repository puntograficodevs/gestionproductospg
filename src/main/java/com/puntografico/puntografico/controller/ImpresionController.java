package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ImpresionDTO;
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
public class ImpresionController {

    private final OpcionesImpresionService opcionesImpresionService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ImpresionService impresionService;
    private final OrdenImpresionService ordenImpresionService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-impresion", "/crear-odt-impresion/{idOrden}"})
    public String verCrearOdtImpresion(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenImpresion ordenImpresion = (idOrden != null) ? ordenImpresionService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenImpresion != null) ? ordenImpresion.getOrdenTrabajo() : new OrdenTrabajo();
        Impresion impresion = (ordenImpresion != null) ? ordenImpresion.getImpresion() : new Impresion();

        List<TipoColorImpresion> listaTipoColorImpresion = opcionesImpresionService.buscarTodosTipoColorImpresion();
        List<TamanioHojaImpresion> listaTamanioHojaImpresion = opcionesImpresionService.buscarTodosTamanioHojaImpresion();
        List<TipoFazImpresion> listaTipoFazImpresion = opcionesImpresionService.buscarTodosTipoFazImpresion();
        List<TipoPapelImpresion> listaTipoPapelImpresion = opcionesImpresionService.buscarTodosTipoPapelImpresion();
        List<CantidadImpresion> listaCantidadImpresion = opcionesImpresionService.buscarTodosCantidadImpresion();
        List<TipoImpresion> listaTipoImpresion = opcionesImpresionService.buscarTodosTipoImpresion();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("impresion", impresion);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoColorImpresion", listaTipoColorImpresion);
        model.addAttribute("listaTamanioHojaImpresion", listaTamanioHojaImpresion);
        model.addAttribute("listaTipoFazImpresion", listaTipoFazImpresion);
        model.addAttribute("listaTipoPapelImpresion", listaTipoPapelImpresion);
        model.addAttribute("listaCantidadImpresion", listaCantidadImpresion);
        model.addAttribute("listaTipoImpresion", listaTipoImpresion);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-impresion";
    }

    @GetMapping("/mostrar-odt-impresion/{ordenImpresionId}")
    public String verOrdenImpresion(@PathVariable("ordenImpresionId") Long ordenImpresionId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenImpresion ordenImpresion = ordenImpresionService.buscarPorId(ordenImpresionId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenImpresion.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenImpresion.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenImpresion.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenImpresion", ordenImpresion);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-impresion";
    }

    @PostMapping("/api/creacion-impresion")
    public String creacionImpresion(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenImpresion ordenImpresionExistente = (idOrden != null) ? ordenImpresionService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenImpresionExistente != null) ? ordenImpresionExistente.getOrdenTrabajo().getId() : null;
        Long idImpresion =  (ordenImpresionExistente != null) ? ordenImpresionExistente.getImpresion().getId() : null;
        Long idOrdenImpresion = (ordenImpresionExistente != null) ? ordenImpresionExistente.getId() : null;

        ImpresionDTO impresionDTO = armarImpresionDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Impresion impresion = impresionService.guardar(impresionDTO, idImpresion);
        OrdenImpresion ordenImpresion = ordenImpresionService.guardar(ordenTrabajo, impresion, idOrdenImpresion);

        return "redirect:/mostrar-odt-impresion/" + ordenImpresion.getId();
    }

    private ImpresionDTO armarImpresionDTO(HttpServletRequest request) {
        ImpresionDTO impresionDTO = new ImpresionDTO();
        impresionDTO.setEsAnillado(request.getParameter("esAnillado") != null);
        impresionDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        impresionDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        impresionDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        impresionDTO.setTipoColorImpresionId(Long.parseLong(request.getParameter("tipoColorImpresion.id")));
        impresionDTO.setTamanioHojaImpresionId(Long.parseLong(request.getParameter("tamanioHojaImpresion.id")));
        impresionDTO.setTipoFazImpresionId(Long.parseLong(request.getParameter("tipoFazImpresion.id")));
        impresionDTO.setTipoPapelImpresionId(Long.parseLong(request.getParameter("tipoPapelImpresion.id")));
        impresionDTO.setCantidadImpresionId(Long.parseLong(request.getParameter("cantidadImpresion.id")));
        impresionDTO.setTipoImpresionId(Long.parseLong(request.getParameter("tipoImpresion.id")));

        return impresionDTO;
    }

    @DeleteMapping("/api/eliminar-orden-impresion/{idOrden}")
    public void eliminarOrdenImpresion(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenImpresion ordenImpresion = ordenImpresionService.buscarPorOrdenId(idOrden);
        Long idOrdenImpresion = ordenImpresion.getId();
        Long idOrdenTrabajo = ordenImpresion.getOrdenTrabajo().getId();
        Long idImpresion = ordenImpresion.getImpresion().getId();

        ordenImpresionService.eliminar(idOrdenImpresion);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        impresionService.eliminar(idImpresion);
    }
}
