package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.LonaPublicitariaDTO;
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
public class LonaPublicitariaController {

    private final OpcionesLonaPublicitariaService opcionesLonaPublicitariaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final LonaPublicitariaService lonaPublicitariaService;
    private final OrdenLonaPublicitariaService ordenLonaPublicitariaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-lona-publicitaria", "/crear-odt-lona-publicitaria/{idOrden}"})
    public String verCrearOdtLonaPublicitaria(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenLonaPublicitaria ordenLonaPublicitaria = (idOrden != null) ? ordenLonaPublicitariaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenLonaPublicitaria != null) ? ordenLonaPublicitaria.getOrdenTrabajo() : new OrdenTrabajo();
        LonaPublicitaria lonaPublicitaria = (ordenLonaPublicitaria != null) ? ordenLonaPublicitaria.getLonaPublicitaria() : new LonaPublicitaria();

        List<MedidaLonaPublicitaria> listaMedidaLonaPublicitaria = opcionesLonaPublicitariaService.buscarTodosMedidaLonaPublicitaria();
        List<TipoLonaPublicitaria> listaTipoLonaPublicitaria = opcionesLonaPublicitariaService.buscarTodosTipoLonaPublicitaria();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("lonaPublicitaria", lonaPublicitaria);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaLonaPublicitaria", listaMedidaLonaPublicitaria);
        model.addAttribute("listaTipoLonaPublicitaria", listaTipoLonaPublicitaria);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-lona-publicitaria";
    }

    @GetMapping("/mostrar-odt-lona-publicitaria/{ordenLonaPublicitariaId}")
    public String verOrdenLonaPublicitaria(@PathVariable("ordenLonaPublicitariaId") Long ordenLonaPublicitariaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenLonaPublicitaria ordenLonaPublicitaria = ordenLonaPublicitariaService.buscarPorId(ordenLonaPublicitariaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenLonaPublicitaria.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenLonaPublicitaria.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenLonaPublicitaria.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenLonaPublicitaria", ordenLonaPublicitaria);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-lona-publicitaria";
    }

    @PostMapping("/api/creacion-lona-publicitaria")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenLonaPublicitaria ordenLonaPublicitariaExistente = (idOrden != null) ? ordenLonaPublicitariaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenLonaPublicitariaExistente != null) ? ordenLonaPublicitariaExistente.getOrdenTrabajo().getId() : null;
        Long idLonaPublicitaria = (ordenLonaPublicitariaExistente != null) ? ordenLonaPublicitariaExistente.getLonaPublicitaria().getId() : null;
        Long idOrdenLonaPublicitaria = (ordenLonaPublicitariaExistente != null) ? ordenLonaPublicitariaExistente.getId() : null;

        LonaPublicitariaDTO lonaPublicitariaDTO = armarLonaPubliciatariaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        LonaPublicitaria lonaPublicitaria = lonaPublicitariaService.guardar(lonaPublicitariaDTO, idLonaPublicitaria);
        OrdenLonaPublicitaria ordenLonaPublicitaria = ordenLonaPublicitariaService.guardar(ordenTrabajo, lonaPublicitaria, idOrdenLonaPublicitaria);

        return "redirect:/mostrar-odt-lona-publicitaria/" + ordenLonaPublicitaria.getId();
    }

    private LonaPublicitariaDTO armarLonaPubliciatariaDTO(HttpServletRequest request) {
        LonaPublicitariaDTO lonaPublicitariaDTO = new LonaPublicitariaDTO();
        lonaPublicitariaDTO.setConAdicionalPortabanner(request.getParameter("conAdicionalPortabanner") != null);
        lonaPublicitariaDTO.setConOjales(request.getParameter("conOjales") != null);
        lonaPublicitariaDTO.setConOjalesConRefuerzo(request.getParameter("conOjalesConRefuerzo") != null);
        lonaPublicitariaDTO.setConBolsillos(request.getParameter("conBolsillos") != null);
        lonaPublicitariaDTO.setConDemasiaParaTensado(request.getParameter("conDemasiaParaTensado") != null);
        lonaPublicitariaDTO.setConSolapado(request.getParameter("conSolapado") != null);
        lonaPublicitariaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        lonaPublicitariaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        lonaPublicitariaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        lonaPublicitariaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        lonaPublicitariaDTO.setMedidaLonaPublicitariaId(Long.parseLong(request.getParameter("medidaLonaPublicitaria.id")));
        lonaPublicitariaDTO.setTipoLonaPublicitariaId(Long.parseLong(request.getParameter("tipoLonaPublicitaria.id")));

        return lonaPublicitariaDTO;
    }

    @DeleteMapping("/api/eliminar-orden-lona-publicitaria/{idOrden}")
    public void eliminarOrdenLonaPublicitaria(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenLonaPublicitaria ordenLonaPublicitaria = ordenLonaPublicitariaService.buscarPorOrdenId(idOrden);
        Long idOrdenLonaPublicitaria = ordenLonaPublicitaria.getId();
        Long idOrdenTrabajo = ordenLonaPublicitaria.getOrdenTrabajo().getId();
        Long idLonaPublicitaria = ordenLonaPublicitaria.getLonaPublicitaria().getId();

        ordenLonaPublicitariaService.eliminar(idOrdenLonaPublicitaria);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        lonaPublicitariaService.eliminar(idLonaPublicitaria);
    }
}
