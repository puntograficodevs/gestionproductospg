package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CuadernoAnilladoDTO;
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
public class CuadernoAnilladoController {

    private final OpcionesCuadernoAnilladoService opcionesCuadernoAnilladoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CuadernoAnilladoService cuadernoAnilladoService;
    private final OrdenCuadernoAnilladoService ordenCuadernoAnilladoService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-cuaderno-anillado", "/crear-odt-cuaderno-anillado/{idOrden}"})
    public String verCrearOdtCuadernoAnillado(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCuadernoAnillado ordenCuadernoAnillado = (idOrden != null) ? ordenCuadernoAnilladoService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenCuadernoAnillado != null) ? ordenCuadernoAnillado.getOrdenTrabajo() : new OrdenTrabajo();
        CuadernoAnillado cuadernoAnillado = (ordenCuadernoAnillado != null)? ordenCuadernoAnillado.getCuadernoAnillado() : new CuadernoAnillado();

        List<TipoTapaCuadernoAnillado> listaTipoTapaCuadernoAnillado = opcionesCuadernoAnilladoService.buscarTodosTipoTapaCuadernoAnillado();
        List<MedidaCuadernoAnillado> listaMedidaCuadernoAnillado = opcionesCuadernoAnilladoService.buscarTodosMedidaCuadernoAnillado();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("cuadernoAnillado", cuadernoAnillado);
        model.addAttribute("listaTipoTapaCuadernoAnillado", listaTipoTapaCuadernoAnillado);
        model.addAttribute("listaMedidaCuadernoAnillado", listaMedidaCuadernoAnillado);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-cuaderno-anillado";
    }

    @GetMapping("/mostrar-odt-cuaderno-anillado/{ordenCuadernoAnilladoId}")
    public String verOrdenCuadernoAnillado(@PathVariable("ordenCuadernoAnilladoId") Long ordenCuadernoAnilladoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCuadernoAnillado ordenCuadernoAnillado = ordenCuadernoAnilladoService.buscarPorId(ordenCuadernoAnilladoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenCuadernoAnillado.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenCuadernoAnillado.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenCuadernoAnillado.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenCuadernoAnillado", ordenCuadernoAnillado);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-cuaderno-anillado";
    }

    @PostMapping("/api/creacion-cuaderno-anillado")
    public String creacionCuadernoAnillado(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenCuadernoAnillado ordenCuadernoAnilladoExistente = (idOrden != null) ? ordenCuadernoAnilladoService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenCuadernoAnilladoExistente != null) ? ordenCuadernoAnilladoExistente.getOrdenTrabajo().getId() : null;
        Long idCuadernoAnillado = (ordenCuadernoAnilladoExistente != null) ? ordenCuadernoAnilladoExistente.getCuadernoAnillado().getId() : null;
        Long idOrdenCuadernoAnillado = (ordenCuadernoAnilladoExistente != null) ? ordenCuadernoAnilladoExistente.getId() : null;

        CuadernoAnilladoDTO cuadernoAnilladoDTO = armarCuadernoAnilladoDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        CuadernoAnillado cuadernoAnillado = cuadernoAnilladoService.guardar(cuadernoAnilladoDTO, idCuadernoAnillado);
        OrdenCuadernoAnillado ordenCuadernoAnillado = ordenCuadernoAnilladoService.guardar(ordenTrabajo, cuadernoAnillado, idOrdenCuadernoAnillado);

        return "redirect:/mostrar-odt-cuaderno-anillado/" + ordenCuadernoAnillado.getId();
    }

    private CuadernoAnilladoDTO armarCuadernoAnilladoDTO(HttpServletRequest request) {
        CuadernoAnilladoDTO cuadernoAnilladoDTO = new CuadernoAnilladoDTO();
        cuadernoAnilladoDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        cuadernoAnilladoDTO.setTipoTapaPersonalizada(request.getParameter("tipoTapaPersonalizada"));
        cuadernoAnilladoDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        cuadernoAnilladoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        cuadernoAnilladoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        cuadernoAnilladoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        cuadernoAnilladoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        cuadernoAnilladoDTO.setMedidaCuadernoAnilladoId(Long.parseLong(request.getParameter("medidaCuadernoAnillado.id")));
        cuadernoAnilladoDTO.setTipoTapaCuadernoAnilladoId(Long.parseLong(request.getParameter("tipoTapaCuadernoAnillado.id")));

        return cuadernoAnilladoDTO;
    }

    @DeleteMapping("/api/eliminar-orden-cuaderno-anillado/{idOrden}")
    public void eliminarOrdenCuadernoAnillado(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenCuadernoAnillado ordenCuadernoAnillado = ordenCuadernoAnilladoService.buscarPorOrdenId(idOrden);
        Long idOrdenCuadernoAnillado = ordenCuadernoAnillado.getId();
        Long idOrdenTrabajo = ordenCuadernoAnillado.getOrdenTrabajo().getId();
        Long idCuadernoAnillado = ordenCuadernoAnillado.getCuadernoAnillado().getId();

        ordenCuadernoAnilladoService.eliminar(idOrdenCuadernoAnillado);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        cuadernoAnilladoService.eliminar(idCuadernoAnillado);
    }
}
