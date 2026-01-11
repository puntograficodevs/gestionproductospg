package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class VoucherController {

    private final OpcionesVoucherService opcionesVoucherService;
    private final MedioPagoService medioPagoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final VoucherService voucherService;

    @GetMapping({"/crear-odts/crear-odt-voucher", "/crear-odts/crear-odt-voucher/{idOrden}"})
    public String verCrearOdtVoucher(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo  = (idOrden != null) ? ordenTrabajoService.buscarPorId(idOrden) : new OrdenTrabajo();
        Voucher voucher = voucherService.buscarPorOrdenTrabajoId(idOrden)
                .orElseGet(Voucher::new);

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

        return "crear-odts/crear-odt-voucher";
    }
}
