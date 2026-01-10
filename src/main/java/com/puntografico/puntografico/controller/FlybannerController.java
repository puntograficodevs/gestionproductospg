package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.FlybannerDTO;
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
public class FlybannerController {

    private final OpcionesFlybannerService opcionesFlybannerService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final FlybannerService flybannerService;
    private final OrdenFlybannerService ordenFlybannerService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-flybanner", "/crear-odt-flybanner/{idOrden}"})
    public String verCrearOdtFlybanner(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenFlybanner ordenFlybanner = (idOrden != null) ? ordenFlybannerService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenFlybanner != null) ? ordenFlybanner.getOrdenTrabajo() : new OrdenTrabajo();
        Flybanner flybanner = (ordenFlybanner != null) ? ordenFlybanner.getFlybanner() : new Flybanner();

        List<TipoFazFlybanner> listaTipoFazFlybanner = opcionesFlybannerService.buscarTodosTipoFazFlybaner();
        List<AlturaFlybanner> listaAlturaFlybanner = opcionesFlybannerService.buscarTodosAlturaFlybanner();
        List<BanderaFlybanner> listaBanderaFlybanner = opcionesFlybannerService.buscarTodosBanderaFlybanner();
        List<TipoBaseFlybanner> listaTipoBaseFlybanner = opcionesFlybannerService.buscarTodosTipoBaseFlybanner();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("flybanner", flybanner);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoFazFlybanner", listaTipoFazFlybanner);
        model.addAttribute("listaAlturaFlybanner", listaAlturaFlybanner);
        model.addAttribute("listaBanderaFlybanner", listaBanderaFlybanner);
        model.addAttribute("listaTipoBaseFlybanner", listaTipoBaseFlybanner);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-flybanner";
    }

    @GetMapping("/mostrar-odt-flybanner/{ordenFlybannerId}")
    public String verOrdenFlybanner(@PathVariable("ordenFlybannerId") Long ordenFlybannerId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenFlybanner ordenFlybanner = ordenFlybannerService.buscarPorId(ordenFlybannerId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenFlybanner.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenFlybanner.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenFlybanner.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenFlybanner", ordenFlybanner);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-flybanner";
    }

    @PostMapping("/api/creacion-flybanner")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenFlybanner ordenFlybannerExistente = (idOrden != null) ? ordenFlybannerService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenFlybannerExistente != null) ? ordenFlybannerExistente.getOrdenTrabajo().getId() : null;
        Long idFlybanner = (ordenFlybannerExistente != null) ? ordenFlybannerExistente.getFlybanner().getId() : null;
        Long idOrdenFlybanner = (ordenFlybannerExistente != null) ? ordenFlybannerExistente.getId() : null;

        FlybannerDTO flybannerDTO = armarFlybannerDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Flybanner flybanner = flybannerService.guardar(flybannerDTO, idFlybanner);
        OrdenFlybanner ordenFlybanner = ordenFlybannerService.guardar(ordenTrabajo, flybanner, idOrdenFlybanner);

        return "redirect:/mostrar-odt-flybanner/" + ordenFlybanner.getId();
    }

    private FlybannerDTO armarFlybannerDTO(HttpServletRequest request) {
        FlybannerDTO flybannerDTO = new FlybannerDTO();
        flybannerDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        flybannerDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        flybannerDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        flybannerDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        flybannerDTO.setTipoFazFlybannerId(Long.parseLong(request.getParameter("tipoFazFlybanner.id")));
        flybannerDTO.setAlturaFlybannerId(Long.parseLong(request.getParameter("alturaFlybanner.id")));
        flybannerDTO.setBanderaFlybannerId(Long.parseLong(request.getParameter("banderaFlybanner.id")));
        flybannerDTO.setTipoBaseFlybannerId(Long.parseLong(request.getParameter("tipoBaseFlybanner.id")));

        return flybannerDTO;
    }

    @DeleteMapping("/api/eliminar-orden-flybanner/{idOrden}")
    public void eliminarOrdenFlybanner(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenFlybanner ordenFlybanner = ordenFlybannerService.buscarPorOrdenId(idOrden);
        Long idOrdenFlybanner = ordenFlybanner.getId();
        Long idOrdenTrabajo = ordenFlybanner.getOrdenTrabajo().getId();
        Long idFlybanner = ordenFlybanner.getFlybanner().getId();

        ordenFlybannerService.eliminar(idOrdenFlybanner);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        flybannerService.eliminar(idFlybanner);
    }
}
