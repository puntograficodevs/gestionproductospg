package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.VoucherDTO;
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
public class VoucherController {

    private final OpcionesVoucherService opcionesVoucherService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final VoucherService voucherService;
    private final OrdenVoucherService ordenVoucherService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-voucher", "/crear-odt-voucher/{idOrden}"})
    public String verCrearOdtVoucher(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenVoucher ordenVoucher = (idOrden != null) ? ordenVoucherService.buscarPorOrdenId(idOrden) : null;
        Voucher voucher = (ordenVoucher != null) ? ordenVoucher.getVoucher() : new Voucher();
        OrdenTrabajo ordenTrabajo = (ordenVoucher != null) ? ordenVoucher.getOrdenTrabajo() : new OrdenTrabajo();

        List<MedidaVoucher> listaMedidaVoucher = opcionesVoucherService.buscarTodosMedidaVoucher();
        List<TipoPapelVoucher> listaTipoPapelVoucher = opcionesVoucherService.buscarTodosTipoPapelVoucher();
        List<TipoFazVoucher> listaTipoFazVoucher = opcionesVoucherService.buscarTodosTipoFazVoucher();
        List<CantidadVoucher> listaCantidadVoucher = opcionesVoucherService.buscarTodosCantidadVoucher();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("voucher", voucher);
        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("listaMedidaVoucher", listaMedidaVoucher);
        model.addAttribute("listaTipoPapelVoucher", listaTipoPapelVoucher);
        model.addAttribute("listaTipoFazVoucher", listaTipoFazVoucher);
        model.addAttribute("listaCantidadVoucher", listaCantidadVoucher);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-voucher";
    }

    @GetMapping("/mostrar-odt-voucher/{ordenVoucherId}")
    public String verOrdenVoucher(@PathVariable("ordenVoucherId") Long ordenVoucherId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenVoucher ordenVoucher = ordenVoucherService.buscarPorId(ordenVoucherId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenVoucher.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenVoucher.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenVoucher.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenVoucher", ordenVoucher);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-voucher";
    }

    @PostMapping("/api/creacion-voucher")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenVoucher ordenVoucherExistente = (idOrden != null) ? ordenVoucherService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenVoucherExistente != null) ? ordenVoucherExistente.getOrdenTrabajo().getId() : null;
        Long idVoucher = (ordenVoucherExistente != null) ? ordenVoucherExistente.getVoucher().getId() : null;
        Long idOrdenVoucher = (ordenVoucherExistente != null) ? ordenVoucherExistente.getId() : null;

        VoucherDTO voucherDTO = armarVoucherDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Voucher voucher = voucherService.guardar(voucherDTO, idVoucher);
        OrdenVoucher ordenVoucher = ordenVoucherService.guardar(ordenTrabajo, voucher, idOrdenVoucher);

        return "redirect:/mostrar-odt-voucher/" + ordenVoucher.getId();
    }

    private VoucherDTO armarVoucherDTO(HttpServletRequest request) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTipoPapelPersonalizado(request.getParameter("tipoPapelPersonalizado"));
        voucherDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        voucherDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        voucherDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        voucherDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        voucherDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        voucherDTO.setMedidaVoucherId(Long.parseLong(request.getParameter("medidaVoucher.id")));
        voucherDTO.setTipoPapelVoucherId(Long.parseLong(request.getParameter("tipoPapelVoucher.id")));
        voucherDTO.setTipoFazVoucherId(Long.parseLong(request.getParameter("tipoFazVoucher.id")));
        voucherDTO.setCantidadVoucherId(Long.parseLong(request.getParameter("cantidadVoucher.id")));

        return voucherDTO;
    }

    @DeleteMapping("/api/eliminar-orden-voucher/{idOrden}")
    public void eliminarOrdenVoucher(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenVoucher ordenVoucher = ordenVoucherService.buscarPorOrdenId(idOrden);
        Long idOrdenVoucher = ordenVoucher.getId();
        Long idOrdenTrabajo = ordenVoucher.getOrdenTrabajo().getId();
        Long idVoucher = ordenVoucher.getVoucher().getId();

        ordenVoucherService.eliminar(idOrdenVoucher);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        voucherService.eliminar(idVoucher);
    }
}
