package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CarpetaSolapaDTO;
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
public class CarpetaSolapaController {

    private final OpcionesCarpetaSolapaService opcionesCarpetaSolapaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CarpetaSolapaService carpetaSolapaService;
    private final OrdenCarpetaSolapaService ordenCarpetaSolapaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-carpeta-solapa", "/crear-odt-carpeta-solapa/{idOrden}"})
    public String verCrearOdtCarpetaSolapa(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCarpetaSolapa ordenCarpetaSolapa = (idOrden != null) ? ordenCarpetaSolapaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenCarpetaSolapa != null) ? ordenCarpetaSolapa.getOrdenTrabajo() : new OrdenTrabajo();
        CarpetaSolapa carpetaSolapa = (ordenCarpetaSolapa != null) ? ordenCarpetaSolapa.getCarpetaSolapa() : new CarpetaSolapa();

        List<TipoFazCarpetaSolapa> listaTipoFazCarpetaSolapa = opcionesCarpetaSolapaService.buscarTodosTipoFazCarpetaSolapa();
        List<TipoLaminadoCarpetaSolapa> listaTipoLaminadoCarpetaSolapa = opcionesCarpetaSolapaService.buscarTodosTipoLaminadoCarpetaSolapa();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("carpetaSolapa", carpetaSolapa);
        model.addAttribute("listaTipoFazCarpetaSolapa", listaTipoFazCarpetaSolapa);
        model.addAttribute("listaTipoLaminadoCarpetaSolapa", listaTipoLaminadoCarpetaSolapa);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-carpeta-solapa";
    }

    @GetMapping("/mostrar-odt-carpeta-solapa/{ordenCarpetaSolapaId}")
    public String verOrdenCarpetaSolapa(@PathVariable("ordenCarpetaSolapaId") Long ordenCarpetaSolapaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCarpetaSolapa ordenCarpetaSolapa = ordenCarpetaSolapaService.buscarPorId(ordenCarpetaSolapaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenCarpetaSolapa.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenCarpetaSolapa.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenCarpetaSolapa.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenCarpetaSolapa", ordenCarpetaSolapa);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-carpeta-solapa";
    }

    @PostMapping("/api/creacion-carpeta-solapa")
    public String creacionCarpetaSolapa(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenCarpetaSolapa ordenCarpetaSolapaExistente = (idOrden != null) ? ordenCarpetaSolapaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenCarpetaSolapaExistente != null) ? ordenCarpetaSolapaExistente.getOrdenTrabajo().getId() : null;
        Long idCarpetaSolapa = (ordenCarpetaSolapaExistente != null) ? ordenCarpetaSolapaExistente.getCarpetaSolapa().getId() : null;
        Long idOrdenCarpetaSolapa = (ordenCarpetaSolapaExistente != null) ? ordenCarpetaSolapaExistente.getId() : null;

        CarpetaSolapaDTO carpetaSolapaDTO = armarCarpetaSolapaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        CarpetaSolapa carpetaSolapa = carpetaSolapaService.guardar(carpetaSolapaDTO, idCarpetaSolapa);
        OrdenCarpetaSolapa ordenCarpetaSolapa = ordenCarpetaSolapaService.guardar(ordenTrabajo, carpetaSolapa, idOrdenCarpetaSolapa);

        return "redirect:/mostrar-odt-carpeta-solapa/" + ordenCarpetaSolapa.getId();
    }

    private CarpetaSolapaDTO armarCarpetaSolapaDTO(HttpServletRequest request) {
        CarpetaSolapaDTO carpetaSolapaDTO = new CarpetaSolapaDTO();
        carpetaSolapaDTO.setTipoFazCarpetaSolapaId(Long.parseLong(request.getParameter("tipoFazCarpetaSolapa.id")));
        carpetaSolapaDTO.setTipoLaminadoCarpetaSolapaId(Long.parseLong(request.getParameter("tipoLaminadoCarpetaSolapa.id")));
        carpetaSolapaDTO.setTipoPapel(request.getParameter("tipoPapel"));
        carpetaSolapaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        carpetaSolapaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        carpetaSolapaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        carpetaSolapaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);

        return carpetaSolapaDTO;
    }

    @DeleteMapping("/api/eliminar-orden-carpeta-solapa/{idOrden}")
    public void eliminarOrdenCarpetaSolapa(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenCarpetaSolapa ordenCarpetaSolapa = ordenCarpetaSolapaService.buscarPorOrdenId(idOrden);
        Long idOrdenCarpetaSolapa = ordenCarpetaSolapa.getId();
        Long idOrdenTrabajo = ordenCarpetaSolapa.getOrdenTrabajo().getId();
        Long idCarpetaSolapa = ordenCarpetaSolapa.getCarpetaSolapa().getId();

        ordenCarpetaSolapaService.eliminar(idOrdenCarpetaSolapa);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        carpetaSolapaService.eliminar(idCarpetaSolapa);
    }
}
