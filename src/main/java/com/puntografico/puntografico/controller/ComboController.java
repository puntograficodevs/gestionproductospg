package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ComboDTO;
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
public class ComboController {

    private final MedioPagoService medioPagoService;
    private final OrdenComboService ordenComboService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OpcionesComboService opcionesComboService;
    private final ComboService comboService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-combo", "/crear-odt-combo/{idOrden}"})
    public String verCrearOdtCombo(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCombo ordenCombo = (idOrden != null) ? ordenComboService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenCombo != null) ? ordenCombo.getOrdenTrabajo() : new OrdenTrabajo();
        Combo combo = (ordenCombo != null) ? ordenCombo.getCombo() : new Combo();

        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();
        List<TipoCombo> listaTipoCombo = opcionesComboService.buscarTodosTipoCombo();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("combo", combo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);
        model.addAttribute("listaTipoCombo", listaTipoCombo);

        return "crear-odt-combo";
    }

    @GetMapping("/mostrar-odt-combo/{ordenComboId}")
    public String verOrdenCombo(@PathVariable("ordenComboId") Long ordenComboId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCombo ordenCombo = ordenComboService.buscarPorId(ordenComboId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenCombo.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenCombo.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenCombo.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenCombo", ordenCombo);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-combo";
    }

    @PostMapping("/api/creacion-combo")
    public String creacionCombo(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenCombo ordenComboExistente = (idOrden != null) ? ordenComboService.buscarPorOrdenId(idOrden): null;
        Long idOrdenTrabajo = (ordenComboExistente != null) ? ordenComboExistente.getOrdenTrabajo().getId() : null;
        Long idCombo = (ordenComboExistente != null) ? ordenComboExistente.getCombo().getId() : null;
        Long idOrdenCombo = (ordenComboExistente != null) ? ordenComboExistente.getId() : null;

        ComboDTO comboDTO = armarComboDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Combo combo = comboService.guardar(comboDTO, idCombo);
        OrdenCombo ordenCombo = ordenComboService.guardar(ordenTrabajo, combo, idOrdenCombo);

        return "redirect:/mostrar-odt-combo/" + ordenCombo.getId();
    }

    private ComboDTO armarComboDTO(HttpServletRequest request) {
        ComboDTO comboDTO = new ComboDTO();
        comboDTO.setTipoComboId(Long.parseLong(request.getParameter("tipoCombo.id")));
        comboDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        comboDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        comboDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        comboDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);

        return comboDTO;
    }

    @DeleteMapping("/api/eliminar-orden-combo/{idOrden}")
    public void eliminarOrdenCombo(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenCombo ordenCombo = ordenComboService.buscarPorOrdenId(idOrden);
        Long idOrdenCombo = ordenCombo.getId();
        Long idOrdenTrabajo = ordenCombo.getOrdenTrabajo().getId();
        Long idCombo = ordenCombo.getCombo().getId();

        ordenComboService.eliminar(idOrdenCombo);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        comboService.eliminar(idCombo);
    }
}
