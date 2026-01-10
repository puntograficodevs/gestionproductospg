package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ViniloPlasticoCorrugadoDTO;
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
public class ViniloPlasticoCorrugadoController {

    private final OpcionesViniloPlasticoCorrugadoService opcionesViniloPlasticoCorrugadoService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final ViniloPlasticoCorrugadoService viniloPlasticoCorrugadoService;
    private final OrdenViniloPlasticoCorrugadoService ordenViniloPlasticoCorrugadoService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-vinilo-plastico-corrugado", "/crear-odt-vinilo-plastico-corrugado/{idOrden}"})
    public String verCrearOdtViniloPlasticoCorrugado(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugado = (idOrden != null) ? ordenViniloPlasticoCorrugadoService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenViniloPlasticoCorrugado != null) ? ordenViniloPlasticoCorrugado.getOrdenTrabajo() : new OrdenTrabajo();
        ViniloPlasticoCorrugado viniloPlasticoCorrugado = (ordenViniloPlasticoCorrugado != null) ? ordenViniloPlasticoCorrugado.getViniloPlasticoCorrugado() : new ViniloPlasticoCorrugado();

        List<MedidaViniloPlasticoCorrugado> listaMedidaViniloPlasticoCorrugado = opcionesViniloPlasticoCorrugadoService.buscarTodosMedidaViniloPlasticoCorrugado();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("viniloPlasticoCorrugado", viniloPlasticoCorrugado);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaViniloPlasticoCorrugado", listaMedidaViniloPlasticoCorrugado);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-vinilo-plastico-corrugado";
    }

    @GetMapping("/mostrar-odt-vinilo-plastico-corrugado/{ordenViniloPlasticoCorrugadoId}")
    public String verOrdenViniloPlasticoCorrugado(@PathVariable("ordenViniloPlasticoCorrugadoId") Long ordenViniloPlasticoCorrugadoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugado = ordenViniloPlasticoCorrugadoService.buscarPorId(ordenViniloPlasticoCorrugadoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenViniloPlasticoCorrugado.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenViniloPlasticoCorrugado.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenViniloPlasticoCorrugado.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenViniloPlasticoCorrugado", ordenViniloPlasticoCorrugado);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-vinilo-plastico-corrugado";
    }

    @PostMapping("/api/creacion-vinilo-plastico-corrugado")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugadoExistente = (idOrden != null) ? ordenViniloPlasticoCorrugadoService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenViniloPlasticoCorrugadoExistente != null) ? ordenViniloPlasticoCorrugadoExistente.getOrdenTrabajo().getId() : null;
        Long idViniloPlasticoCorrugado = (ordenViniloPlasticoCorrugadoExistente != null) ? ordenViniloPlasticoCorrugadoExistente.getViniloPlasticoCorrugado().getId() : null;
        Long idOrdenViniloPlasticoCorrugado = (ordenViniloPlasticoCorrugadoExistente != null) ? ordenViniloPlasticoCorrugadoExistente.getId() : null;

        ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO = armarViniloPlasticoCorrugadoDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        ViniloPlasticoCorrugado viniloPlasticoCorrugado = viniloPlasticoCorrugadoService.guardar(viniloPlasticoCorrugadoDTO, idViniloPlasticoCorrugado);
        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugado = ordenViniloPlasticoCorrugadoService.guardar(ordenTrabajo, viniloPlasticoCorrugado, idOrdenViniloPlasticoCorrugado);

        return "redirect:/mostrar-odt-vinilo-plastico-corrugado/" + ordenViniloPlasticoCorrugado.getId();
    }

    private ViniloPlasticoCorrugadoDTO armarViniloPlasticoCorrugadoDTO(HttpServletRequest request) {
        ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO = new ViniloPlasticoCorrugadoDTO();
        viniloPlasticoCorrugadoDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        viniloPlasticoCorrugadoDTO.setConOjales(request.getParameter("conOjales") != null);
        viniloPlasticoCorrugadoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        viniloPlasticoCorrugadoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        viniloPlasticoCorrugadoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        viniloPlasticoCorrugadoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        viniloPlasticoCorrugadoDTO.setMedidaViniloPlasticoCorrugadoId(Long.parseLong(request.getParameter("medidaViniloPlasticoCorrugado.id")));

        return viniloPlasticoCorrugadoDTO;
    }

    @DeleteMapping("/api/eliminar-orden-vinilo-plastico-corrugado/{idOrden}")
    public void eliminarOrdenEtiqueta(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugado = ordenViniloPlasticoCorrugadoService.buscarPorOrdenId(idOrden);
        Long idOrdenViniloPlasticoCorrugado = ordenViniloPlasticoCorrugado.getId();
        Long idOrdenTrabajo = ordenViniloPlasticoCorrugado.getOrdenTrabajo().getId();
        Long idViniloPlasticoCorrugado = ordenViniloPlasticoCorrugado.getViniloPlasticoCorrugado().getId();

        ordenViniloPlasticoCorrugadoService.eliminar(idOrdenViniloPlasticoCorrugado);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        viniloPlasticoCorrugadoService.eliminar(idViniloPlasticoCorrugado);
    }
}
