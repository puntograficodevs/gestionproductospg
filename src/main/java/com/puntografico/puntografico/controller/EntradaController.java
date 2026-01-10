package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.EntradaDTO;
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
public class EntradaController {

    private final OpcionesEntradaService opcionesEntradaService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final EntradaService entradaService;
    private final OrdenEntradaService ordenEntradaService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-entrada", "/crear-odt-entrada/{idOrden}"})
    public String verCrearOdtEntrada(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenEntrada ordenEntrada = (idOrden != null) ? ordenEntradaService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenEntrada != null) ? ordenEntrada.getOrdenTrabajo() : new OrdenTrabajo();
        Entrada entrada = (ordenEntrada != null) ? ordenEntrada.getEntrada() : new Entrada();

        List<TipoPapelEntrada> listaTipoPapelEntrada = opcionesEntradaService.buscarTodosTipoPapelEntrada();
        List<TipoColorEntrada> listaTipoColorEntrada = opcionesEntradaService.buscarTodosTipoColorEntrada();
        List<TipoTroqueladoEntrada> listaTipoTroqueladoEntrada = opcionesEntradaService.buscarTodosTipoTroqueladoEntrada();
        List<MedidaEntrada> listaMedidaEntrada = opcionesEntradaService.buscarTodosMedidaEntrada();
        List<CantidadEntrada> listaCantidadEntrada = opcionesEntradaService.buscarTodosCantidadEntrada();
        List<NumeradoEntrada> listaNumeradoEntrada = opcionesEntradaService.buscarTodosNumeradoEntrada();
        List<TerminacionEntrada> listaTerminacionEntrada = opcionesEntradaService.buscarTodosTerminacionEntrada();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("entrada", entrada);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoPapelEntrada", listaTipoPapelEntrada);
        model.addAttribute("listaTipoColorEntrada", listaTipoColorEntrada);
        model.addAttribute("listaTipoTroqueladoEntrada", listaTipoTroqueladoEntrada);
        model.addAttribute("listaMedidaEntrada", listaMedidaEntrada);
        model.addAttribute("listaCantidadEntrada", listaCantidadEntrada);
        model.addAttribute("listaNumeradoEntrada", listaNumeradoEntrada);
        model.addAttribute("listaTerminacionEntrada", listaTerminacionEntrada);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-entrada";
    }

    @GetMapping("/mostrar-odt-entrada/{ordenEntradaId}")
    public String verOrdenEntrada(@PathVariable("ordenEntradaId") Long ordenEntradaId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenEntrada ordenEntrada = ordenEntradaService.buscarPorId(ordenEntradaId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenEntrada.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenEntrada.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenEntrada.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenEntrada", ordenEntrada);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-entrada";
    }

    @PostMapping("/api/creacion-entrada")
    public String creacionEntrada(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenEntrada ordenEntradaExistente = (idOrden != null) ? ordenEntradaService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenEntradaExistente != null) ? ordenEntradaExistente.getOrdenTrabajo().getId() : null;
        Long idEntrada = (ordenEntradaExistente != null) ? ordenEntradaExistente.getEntrada().getId() : null;
        Long idOrdenEntrada = (ordenEntradaExistente != null) ? ordenEntradaExistente.getId() : null;

        EntradaDTO entradaDTO = armarEntradaDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Entrada entrada = entradaService.guardar(entradaDTO, idEntrada);
        OrdenEntrada ordenEntrada = ordenEntradaService.guardar(ordenTrabajo, entrada, idOrdenEntrada);

        return "redirect:/mostrar-odt-entrada/" + ordenEntrada.getId();
    }

    private EntradaDTO armarEntradaDTO(HttpServletRequest request) {
        EntradaDTO entradaDTO = new EntradaDTO();
        entradaDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        entradaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        entradaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        entradaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        entradaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        entradaDTO.setTipoPapelEntradaId(Long.parseLong(request.getParameter("tipoPapelEntrada.id")));
        entradaDTO.setTipoColorEntradaId(Long.parseLong(request.getParameter("tipoColorEntrada.id")));
        entradaDTO.setTipoTroqueladoEntradaId(Long.parseLong(request.getParameter("tipoTroqueladoEntrada.id")));
        entradaDTO.setMedidaEntradaId(Long.parseLong(request.getParameter("medidaEntrada.id")));
        entradaDTO.setCantidadEntradaId(Long.parseLong(request.getParameter("cantidadEntrada.id")));
        entradaDTO.setNumeradoEntradaId(Long.parseLong(request.getParameter("numeradoEntrada.id")));
        entradaDTO.setTerminacionEntradaId(Long.parseLong(request.getParameter("terminacionEntrada.id")));

        return entradaDTO;
    }

    @DeleteMapping("/api/eliminar-orden-entrada/{idOrden}")
    public void eliminarOrdenEntrada(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenEntrada ordenEntrada = ordenEntradaService.buscarPorOrdenId(idOrden);
        Long idOrdenEntrada = ordenEntrada.getId();
        Long idOrdenTrabajo = ordenEntrada.getOrdenTrabajo().getId();
        Long idEntrada = ordenEntrada.getEntrada().getId();

        ordenEntradaService.eliminar(idOrdenEntrada);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        entradaService.eliminar(idEntrada);
    }
}
