package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.OtroDTO;
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
public class OtroController {

    private final OpcionesOtroService opcionesOtroService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final OtroService otroService;
    private final OrdenOtroService ordenOtroService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-otro", "/crear-odt-otro/{idOrden}"})
    public String verCrearOdtOtro(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenOtro ordenOtro = (idOrden != null) ? ordenOtroService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenOtro != null) ? ordenOtro.getOrdenTrabajo() : new OrdenTrabajo();
        Otro otro = (ordenOtro != null) ? ordenOtro.getOtro() : new Otro();

        List<TipoColorOtro> listaTipoColorOtro = opcionesOtroService.buscarTodosTipoColorOtro();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("otro", otro);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaTipoColorOtro", listaTipoColorOtro);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-otro";
    }

    @GetMapping("/mostrar-odt-otro/{ordenOtroId}")
    public String verOrdenOtro(@PathVariable("ordenOtroId") Long ordenOtroId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenOtro ordenOtro = ordenOtroService.buscarPorId(ordenOtroId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenOtro.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenOtro.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenOtro.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenOtro", ordenOtro);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-otro";
    }

    @PostMapping("/api/creacion-otro")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenOtro ordenOtroExistente = (idOrden != null) ? ordenOtroService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenOtroExistente != null) ? ordenOtroExistente.getOrdenTrabajo().getId() : null;
        Long idOtro = (ordenOtroExistente != null) ? ordenOtroExistente.getOtro().getId() : null;
        Long idOrdenOtro = (ordenOtroExistente != null) ? ordenOtroExistente.getId() : null;

        OtroDTO otroDTO = armarOtroDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Otro otro = otroService.guardar(otroDTO, idOtro);
        OrdenOtro ordenOtro = ordenOtroService.guardar(ordenTrabajo, otro, idOrdenOtro);

        return "redirect:/mostrar-odt-otro/" + ordenOtro.getId();
    }

    private OtroDTO armarOtroDTO(HttpServletRequest request) {
        OtroDTO otroDTO = new OtroDTO();
        otroDTO.setMedida(request.getParameter("medida"));
        otroDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        otroDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        otroDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        otroDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        otroDTO.setTipoColorOtroId(Long.parseLong(request.getParameter("tipoColorOtro.id")));

        return otroDTO;
    }

    @DeleteMapping("/api/eliminar-orden-otro/{idOrden}")
    public void eliminarOrdenOtro(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenOtro ordenOtro = ordenOtroService.buscarPorOrdenId(idOrden);
        Long idOrdenOtro = ordenOtro.getId();
        Long idOrdenTrabajo = ordenOtro.getOrdenTrabajo().getId();
        Long idOtro = ordenOtro.getOtro().getId();

        ordenOtroService.eliminar(idOrdenOtro);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        otroService.eliminar(idOtro);
    }
}
