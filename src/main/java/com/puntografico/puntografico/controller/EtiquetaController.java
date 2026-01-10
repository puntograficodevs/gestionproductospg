package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.EtiquetaDTO;
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
public class EtiquetaController {

    private final OpcionesEtiquetaService opcionesEtiquetaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final EtiquetaService etiquetaService;
    private final OrdenEtiquetaService ordenEtiquetaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-etiqueta", "/crear-odt-etiqueta/{idOrden}"})
    public String verCrearOdtEtiqueta(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenEtiqueta ordenEtiqueta = (idOrden != null) ? ordenEtiquetaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenEtiqueta != null) ? ordenEtiqueta.getOrdenTrabajo() : new OrdenTrabajo();
        Etiqueta etiqueta = (ordenEtiqueta != null) ? ordenEtiqueta.getEtiqueta() : new Etiqueta();

        List<TipoPapelEtiqueta> listaTipoPapelEtiqueta = opcionesEtiquetaService.buscarTodosTipoPapelEtiqueta();
        List<TipoLaminadoEtiqueta> listaTipoLaminadoEtiqueta = opcionesEtiquetaService.buscarTodosTipoLaminadoEtiqueta();
        List<TamanioPerforacion> listaTamanioPerforacion = opcionesEtiquetaService.buscarTodosTamanioPerforacion();
        List<TipoFazEtiqueta> listaTipoFazEtiqueta = opcionesEtiquetaService.buscarTodosTipoFazEtiqueta();
        List<CantidadEtiqueta> listaCantidadEtiqueta = opcionesEtiquetaService.buscarTodosCantidadEtiqueta();
        List<MedidaEtiqueta> listaMedidaEtiqueta = opcionesEtiquetaService.buscarTodosMedidaEtiqueta();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("etiqueta", etiqueta);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelEtiqueta", listaTipoPapelEtiqueta);
        model.addAttribute("listaTipoLaminadoEtiqueta", listaTipoLaminadoEtiqueta);
        model.addAttribute("listaTamanioPerforacion", listaTamanioPerforacion);
        model.addAttribute("listaTipoFazEtiqueta", listaTipoFazEtiqueta);
        model.addAttribute("listaCantidadEtiqueta", listaCantidadEtiqueta);
        model.addAttribute("listaMedidaEtiqueta", listaMedidaEtiqueta);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-etiqueta";
    }

    @GetMapping("/mostrar-odt-etiqueta/{ordenEtiquetaId}")
    public String verOrdenEtiqueta(@PathVariable("ordenEtiquetaId") Long ordenEtiquetaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenEtiqueta ordenEtiqueta = ordenEtiquetaService.buscarPorId(ordenEtiquetaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenEtiqueta.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenEtiqueta.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenEtiqueta.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenEtiqueta", ordenEtiqueta);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-etiqueta";
    }

    @PostMapping("/api/creacion-etiqueta")
    public String creacionEtiqueta(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenEtiqueta ordenEtiquetaExistente = (idOrden != null) ? ordenEtiquetaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenEtiquetaExistente != null) ? ordenEtiquetaExistente.getOrdenTrabajo().getId() : null;
        Long idEtiqueta = (ordenEtiquetaExistente != null) ? ordenEtiquetaExistente.getEtiqueta().getId() : null;
        Long idOrdenEtiqueta = (ordenEtiquetaExistente != null) ? ordenEtiquetaExistente.getId() : null;

        EtiquetaDTO etiquetaDTO = armarEtiquetaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Etiqueta etiqueta = etiquetaService.guardar(etiquetaDTO, idEtiqueta);
        OrdenEtiqueta ordenEtiqueta = ordenEtiquetaService.guardar(ordenTrabajo, etiqueta, idOrdenEtiqueta);

        return "redirect:/mostrar-odt-etiqueta/" + ordenEtiqueta.getId();
    }

    private EtiquetaDTO armarEtiquetaDTO(HttpServletRequest request) {
        EtiquetaDTO etiquetaDTO = new EtiquetaDTO();
        etiquetaDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        etiquetaDTO.setConPerforacionAdicional(request.getParameter("conPerforacionAdicional") != null);
        etiquetaDTO.setConMarcaAdicional(request.getParameter("conMarcaAdicional") != null);
        etiquetaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        etiquetaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        etiquetaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        etiquetaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        etiquetaDTO.setTipoPapelEtiquetaId(Long.parseLong(request.getParameter("tipoPapelEtiqueta.id")));
        etiquetaDTO.setTipoLaminadoEtiquetaId(Long.parseLong(request.getParameter("tipoLaminadoEtiqueta.id")));
        etiquetaDTO.setTamanioPerforacionId(Long.parseLong(request.getParameter("tamanioPerforacion.id")));
        etiquetaDTO.setTipoFazEtiquetaId(Long.parseLong(request.getParameter("tipoFazEtiqueta.id")));
        etiquetaDTO.setCantidadEtiquetaId(Long.parseLong(request.getParameter("cantidadEtiqueta.id")));
        etiquetaDTO.setMedidaEtiquetaId(Long.parseLong(request.getParameter("medidaEtiqueta.id")));

        return etiquetaDTO;
    }

    @DeleteMapping("/api/eliminar-orden-etiqueta/{idOrden}")
    public void eliminarOrdenEtiqueta(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenEtiqueta ordenEtiqueta = ordenEtiquetaService.buscarPorOrdenId(idOrden);
        Long idOrdenEtiqueta = ordenEtiqueta.getId();
        Long idOrdenTrabajo = ordenEtiqueta.getOrdenTrabajo().getId();
        Long idEtiqueta = ordenEtiqueta.getEtiqueta().getId();

        ordenEtiquetaService.eliminar(idOrdenEtiqueta);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        etiquetaService.eliminar(idEtiqueta);
    }
}
