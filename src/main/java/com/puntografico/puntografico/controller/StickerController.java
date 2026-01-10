package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.StickerDTO;
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
public class StickerController {

    private final StickerService stickerService;
    private final MedioPagoService medioPagoService;
    private final OpcionesStickerService opcionesStickerService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OrdenStickerService ordenStickerService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-sticker", "/crear-odt-sticker/{idOrden}"})
    public String verCrearOdtSticker(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSticker ordenSticker = (idOrden != null) ? ordenStickerService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenSticker != null) ? ordenSticker.getOrdenTrabajo() : new OrdenTrabajo();
        Sticker sticker = (ordenSticker != null) ? ordenSticker.getSticker() : new Sticker();

        List<TipoTroqueladoSticker> listaTipoTroqueladoSticker = opcionesStickerService.buscarTodosTipoTroqueladoSticker();
        List<CantidadSticker> listaCantidadSticker = opcionesStickerService.buscarTodosCantidadSticker();
        List<MedidaSticker> listaMedidaSticker = opcionesStickerService.buscarTodosMedidaSticker();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("sticker", sticker);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoTroqueladoSticker", listaTipoTroqueladoSticker);
        model.addAttribute("listaCantidadSticker", listaCantidadSticker);
        model.addAttribute("listaMedidaSticker", listaMedidaSticker);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-sticker";
    }

    @GetMapping("/mostrar-odt-sticker/{ordenStickerId}")
    public String verOrdenSticker(@PathVariable("ordenStickerId") Long ordenStickerId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenSticker ordenSticker = ordenStickerService.buscarPorId(ordenStickerId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenSticker.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenSticker.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenSticker.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenSticker", ordenSticker);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-sticker";
    }

    @PostMapping("/api/creacion-sticker")
    public String creacionSticker(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenSticker ordenStickerExistente = (idOrden != null) ? ordenStickerService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenSticker = (ordenStickerExistente != null) ? ordenStickerExistente.getId() : null;
        Long idOrdenTrabajo = (ordenStickerExistente != null) ? ordenStickerExistente.getOrdenTrabajo().getId() : null;
        Long idSticker = (ordenStickerExistente != null) ? ordenStickerExistente.getSticker().getId() : null;

        StickerDTO stickerDTO = armarStickerDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Sticker sticker = stickerService.guardar(stickerDTO, idSticker);
        OrdenSticker ordenSticker = ordenStickerService.guardar(ordenTrabajo, sticker, idOrdenSticker);

        return "redirect:/mostrar-odt-sticker/" + ordenSticker.getId();
    }

    private StickerDTO armarStickerDTO(HttpServletRequest request) {
        StickerDTO stickerDTO = new StickerDTO();
        stickerDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        stickerDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        stickerDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        stickerDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        stickerDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        stickerDTO.setTipoTroqueladoStickerId(Long.parseLong(request.getParameter("tipoTroqueladoSticker.id")));
        stickerDTO.setCantidadStickerId(Long.parseLong(request.getParameter("cantidadSticker.id")));
        stickerDTO.setMedidaStickerId(Long.parseLong(request.getParameter("medidaSticker.id")));

        return stickerDTO;
    }

    @DeleteMapping("/api/eliminar-orden-sticker/{idOrden}")
    public void eliminarOrdenSticker(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenSticker ordenSticker = ordenStickerService.buscarPorOrdenId(idOrden);
        Long idOrdenSticker = ordenSticker.getId();
        Long idOrdenTrabajo = ordenSticker.getOrdenTrabajo().getId();
        Long idSticker = ordenSticker.getSticker().getId();

        ordenStickerService.eliminar(idOrdenSticker);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        stickerService.eliminar(idSticker);
    }
}
