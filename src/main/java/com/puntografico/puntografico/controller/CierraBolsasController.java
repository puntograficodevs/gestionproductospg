package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CierraBolsasDTO;
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
public class CierraBolsasController {

    private final OpcionesCierraBolsasService opcionesCierraBolsasService;
    private final MedioPagoService medioPagoService;
    private final OrdenCierraBolsasService ordenCierraBolsasService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CierraBolsasService cierraBolsasService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-cierra-bolsas", "/crear-odt-cierra-bolsas/{idOrden}"})
    public String verCrearOdtCierraBolsas(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCierraBolsas ordenCierraBolsas = (idOrden != null) ? ordenCierraBolsasService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenCierraBolsas != null) ? ordenCierraBolsas.getOrdenTrabajo() : new OrdenTrabajo();
        CierraBolsas cierraBolsas = (ordenCierraBolsas != null) ? ordenCierraBolsas.getCierraBolsas() : new CierraBolsas();

        List<TipoTroqueladoCierraBolsas> listaTipoTroqueladoCierraBolsas = opcionesCierraBolsasService.buscarTodosTipoTroqueladoCierraBolsas();
        List<MedidaCierraBolsas> listaMedidaCierraBolsas = opcionesCierraBolsasService.buscarTodosMedidaCierraBolsas();
        List<CantidadCierraBolsas> listaCantidadCierraBolsas = opcionesCierraBolsasService.buscarTodosCantidadCierraBolsas();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("cierraBolsas", cierraBolsas);
        model.addAttribute("listaTipoTroqueladoCierraBolsas", listaTipoTroqueladoCierraBolsas);
        model.addAttribute("listaMedidaCierraBolsas", listaMedidaCierraBolsas);
        model.addAttribute("listaCantidadCierraBolsas", listaCantidadCierraBolsas);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-cierra-bolsas";
    }

    @GetMapping("/mostrar-odt-cierra-bolsas/{ordenCierraBolsasId}")
    public String verOrdenCierraBolsas(@PathVariable("ordenCierraBolsasId") Long ordenCierraBolsasId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCierraBolsas ordenCierraBolsas = ordenCierraBolsasService.buscarPorId(ordenCierraBolsasId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenCierraBolsas.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenCierraBolsas.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenCierraBolsas.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenCierraBolsas", ordenCierraBolsas);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-cierra-bolsas";
    }

    @PostMapping("/api/creacion-cierra-bolsas")
    public String creacionCierraBolsas(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenCierraBolsas ordenCierraBolsasExistente = (idOrden != null) ? ordenCierraBolsasService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenCierraBolsasExistente != null) ? ordenCierraBolsasExistente.getOrdenTrabajo().getId() : null;
        Long idCierraBolsas = (ordenCierraBolsasExistente != null) ? ordenCierraBolsasExistente.getCierraBolsas().getId() : null;
        Long idOrdenCierraBolsas = (ordenCierraBolsasExistente != null) ? ordenCierraBolsasExistente.getId() : null;

        CierraBolsasDTO cierraBolsasDTO = armarCierraBolsasDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        CierraBolsas cierraBolsas = cierraBolsasService.guardar(cierraBolsasDTO, idCierraBolsas);
        OrdenCierraBolsas ordenCierraBolsas = ordenCierraBolsasService.guardar(ordenTrabajo, cierraBolsas, idOrdenCierraBolsas);

        return "redirect:/mostrar-odt-cierra-bolsas/" + ordenCierraBolsas.getId();
    }

    private CierraBolsasDTO armarCierraBolsasDTO(HttpServletRequest request) {
        CierraBolsasDTO cierraBolsasDTO = new CierraBolsasDTO();
        cierraBolsasDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        cierraBolsasDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        cierraBolsasDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        cierraBolsasDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        cierraBolsasDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        cierraBolsasDTO.setTipoTroqueladoCierraBolsasId(Long.parseLong(request.getParameter("tipoTroqueladoCierraBolsas.id")));
        cierraBolsasDTO.setCantidadCierraBolsasId(Long.parseLong(request.getParameter("cantidadCierraBolsas.id")));
        cierraBolsasDTO.setMedidaCierraBolsasId(Long.parseLong(request.getParameter("medidaCierraBolsas.id")));

        return cierraBolsasDTO;
    }

    @DeleteMapping("/api/eliminar-orden-cierra-bolsas/{idOrden}")
    public void eliminarOrdenCierraBolsas(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenCierraBolsas ordenCierraBolsas = ordenCierraBolsasService.buscarPorOrdenId(idOrden);
        Long idOrdenCierraBolsas = ordenCierraBolsas.getId();
        Long idOrdenTrabajo = ordenCierraBolsas.getOrdenTrabajo().getId();
        Long idCierraBolsas = ordenCierraBolsas.getCierraBolsas().getId();

        ordenCierraBolsasService.eliminar(idOrdenCierraBolsas);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        cierraBolsasService.eliminar(idCierraBolsas);
    }
}
