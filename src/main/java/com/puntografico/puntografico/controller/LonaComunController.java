package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.LonaComunDTO;
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
public class LonaComunController {

    private final OpcionesLonaComunService opcionesLonaComunService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final LonaComunService lonaComunService;
    private final OrdenLonaComunService ordenLonaComunService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-lona-comun", "/crear-odt-lona-comun/{idOrden}"})
    public String verCrearOdtLonaComun(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenLonaComun ordenLonaComun = (idOrden != null) ? ordenLonaComunService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenLonaComun != null) ? ordenLonaComun.getOrdenTrabajo() : new OrdenTrabajo();
        LonaComun lonaComun = (ordenLonaComun != null) ? ordenLonaComun.getLonaComun() : new LonaComun();

        List<MedidaLonaComun> listaMedidaLonaComun = opcionesLonaComunService.buscarTodosMedidaLonaComun();
        List<TipoLonaComun> listaTipoLonaComun = opcionesLonaComunService.buscarTodosTipoLonaComun();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("lonaComun", lonaComun);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaLonaComun", listaMedidaLonaComun);
        model.addAttribute("listaTipoLonaComun", listaTipoLonaComun);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-lona-comun";
    }

    @GetMapping("/mostrar-odt-lona-comun/{ordenLonaComunId}")
    public String verOrdenLonaComun(@PathVariable("ordenLonaComunId") Long ordenLonaComunId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenLonaComun ordenLonaComun = ordenLonaComunService.buscarPorId(ordenLonaComunId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenLonaComun.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenLonaComun.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenLonaComun.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenLonaComun", ordenLonaComun);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-lona-comun";
    }

    @PostMapping("/api/creacion-lona-comun")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenLonaComun ordenLonaComunExistente = (idOrden != null) ? ordenLonaComunService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenLonaComunExistente != null) ? ordenLonaComunExistente.getOrdenTrabajo().getId() : null;
        Long idLonaComun = (ordenLonaComunExistente != null) ? ordenLonaComunExistente.getLonaComun().getId() : null;
        Long idOrdenLonaComoun = (ordenLonaComunExistente != null) ? ordenLonaComunExistente.getId() : null;

        LonaComunDTO lonaComunDTO = armarLonaComunDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        LonaComun lonaComun = lonaComunService.guardar(lonaComunDTO, idLonaComun);
        OrdenLonaComun ordenLonaComun = ordenLonaComunService.guardar(ordenTrabajo, lonaComun, idOrdenLonaComoun);

        return "redirect:/mostrar-odt-lona-comun/" + ordenLonaComun.getId();
    }

    private LonaComunDTO armarLonaComunDTO(HttpServletRequest request) {
        LonaComunDTO lonaComunDTO = new LonaComunDTO();
        lonaComunDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        lonaComunDTO.setConOjales(request.getParameter("conOjales") != null);
        lonaComunDTO.setConOjalesConRefuerzo(request.getParameter("conOjalesConRefuerzo") != null);
        lonaComunDTO.setConBolsillos(request.getParameter("conBolsillos") != null);
        lonaComunDTO.setConDemasiaParaTensado(request.getParameter("conDemasiaParaTensado") != null);
        lonaComunDTO.setConSolapado(request.getParameter("conSolapado") != null);
        lonaComunDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        lonaComunDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        lonaComunDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        lonaComunDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        lonaComunDTO.setMedidaLonaComunId(Long.parseLong(request.getParameter("medidaLonaComun.id")));
        lonaComunDTO.setTipoLonaComunId(Long.parseLong(request.getParameter("tipoLonaComun.id")));

        return lonaComunDTO;
    }

    @DeleteMapping("/api/eliminar-orden-lona-comun/{idOrden}")
    public void eliminarOrdenLonaComun(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenLonaComun ordenLonaComun = ordenLonaComunService.buscarPorOrdenId(idOrden);
        Long idOrdenLonaComun = ordenLonaComun.getId();
        Long idOrdenTrabajo = ordenLonaComun.getOrdenTrabajo().getId();
        Long idLonaComun = ordenLonaComun.getLonaComun().getId();

        ordenLonaComunService.eliminar(idOrdenLonaComun);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        lonaComunService.eliminar(idLonaComun);
    }
}
