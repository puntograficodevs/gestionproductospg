package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ViniloDeCorteDTO;
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
public class ViniloDeCorteController {

    private final OpcionesViniloDeCorteService opcionesViniloDeCorteService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ViniloDeCorteService viniloDeCorteService;
    private final OrdenViniloDeCorteService ordenViniloDeCorteService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-vinilo-de-corte", "/crear-odt-vinilo-de-corte/{idOrden}"})
    public String verCrearOdtViniloDeCorte(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenViniloDeCorte ordenViniloDeCorte = (idOrden != null) ? ordenViniloDeCorteService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenViniloDeCorte != null) ? ordenViniloDeCorte.getOrdenTrabajo() : new OrdenTrabajo();
        ViniloDeCorte viniloDeCorte = (ordenViniloDeCorte != null) ? ordenViniloDeCorte.getViniloDeCorte() : new ViniloDeCorte();

        List<TraeMaterialVinilo> listaTraeMaterialVinilo = opcionesViniloDeCorteService.buscarTodosTraeMaterialVinilo();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("viniloDeCorte", viniloDeCorte);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTraeMaterialVinilo", listaTraeMaterialVinilo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-vinilo-de-corte";
    }

    @GetMapping("/mostrar-odt-vinilo-de-corte/{ordenViniloDeCorteId}")
    public String verOrdenViniloDeCorte(@PathVariable("ordenViniloDeCorteId") Long ordenViniloDeCorteId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenViniloDeCorte ordenViniloDeCorte = ordenViniloDeCorteService.buscarPorId(ordenViniloDeCorteId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenViniloDeCorte.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenViniloDeCorte.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenViniloDeCorte.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenViniloDeCorte", ordenViniloDeCorte);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-vinilo-de-corte";
    }

    @PostMapping("/api/creacion-vinilo-de-corte")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenViniloDeCorte ordenViniloDeCorteExistente = (idOrden != null) ? ordenViniloDeCorteService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenViniloDeCorteExistente != null) ? ordenViniloDeCorteExistente.getOrdenTrabajo().getId() : null;
        Long idViniloDeCorte = (ordenViniloDeCorteExistente != null) ? ordenViniloDeCorteExistente.getViniloDeCorte().getId() : null;
        Long idOrdenViniloDeCorte = (ordenViniloDeCorteExistente != null) ? ordenViniloDeCorteExistente.getId() : null;

        ViniloDeCorteDTO viniloDeCorteDTO = armarViniloDeCorteDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        ViniloDeCorte viniloDeCorte = viniloDeCorteService.guardar(viniloDeCorteDTO, idViniloDeCorte);
        OrdenViniloDeCorte ordenViniloDeCorte = ordenViniloDeCorteService.guardar(ordenTrabajo, viniloDeCorte, idOrdenViniloDeCorte);

        return "redirect:/mostrar-odt-vinilo-de-corte/" + ordenViniloDeCorte.getId();
    }

    private ViniloDeCorteDTO armarViniloDeCorteDTO(HttpServletRequest request) {
        ViniloDeCorteDTO viniloDeCorteDTO = new ViniloDeCorteDTO();
        viniloDeCorteDTO.setEsPromocional(request.getParameter("esPromocional") != null);
        viniloDeCorteDTO.setEsOracal(request.getParameter("esOracal") != null);
        viniloDeCorteDTO.setCodigoColor(request.getParameter("codigoColor"));
        viniloDeCorteDTO.setConColocacion(request.getParameter("conColocacion") != null);
        viniloDeCorteDTO.setMedida(request.getParameter("medida"));
        viniloDeCorteDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        viniloDeCorteDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        viniloDeCorteDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        viniloDeCorteDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        viniloDeCorteDTO.setTraeMaterialViniloId(Long.parseLong(request.getParameter("traeMaterialVinilo.id")));

        return viniloDeCorteDTO;
    }

    @DeleteMapping("/api/eliminar-orden-vinilo-de-corte/{idOrden}")
    public void eliminarOrdenViniloDeCorte(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenViniloDeCorte ordenViniloDeCorte = ordenViniloDeCorteService.buscarPorOrdenId(idOrden);
        Long idOrdenViniloDeCorte = ordenViniloDeCorte.getId();
        Long idOrdenTrabajo = ordenViniloDeCorte.getOrdenTrabajo().getId();
        Long idViniloDeCorte = ordenViniloDeCorte.getViniloDeCorte().getId();

        ordenViniloDeCorteService.eliminar(idOrdenViniloDeCorte);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        viniloDeCorteService.eliminar(idViniloDeCorte);
    }
}
