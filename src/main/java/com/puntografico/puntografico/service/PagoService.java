package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.MedioPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
@Transactional
@AllArgsConstructor
public class PagoService {

    private final MedioPagoRepository medioPagoRepository;

    public Orden guardar(Orden orden, Long idMedioPago) {
        MedioPago medioPago = medioPagoRepository.findById(idMedioPago)
                .orElseThrow(() -> new RuntimeException("No existe el medio de pago"));

        Pago pago = new Pago();
        pago.setFechaPago(LocalDate.now());
        pago.setMedioPago(medioPago);
        pago.setImporte(orden.getAbonado());
        pago.setEmpleado(orden.getEmpleado());
        pago.setOrden(orden);

        if (orden.getPagos() == null) {
            orden.setPagos(new ArrayList<>());
        }
        orden.getPagos().add(pago);

        return orden;
    }
/*
    private final PagoRepository pagoRepository;
    private final OrdenRepository ordenRepository;

    public void guardar(HttpServletRequest request, Long idOrdenTrabajo) {
        int abonado = Integer.parseInt(request.getParameter("abonado"));

        if (abonado != 0) {
            MedioPago medioPago = obtenerMedioPagoDesdeRequest(request);
            Empleado empleado = obtenerEmpleadoDesdeReequest(request);
            int importe = obtenerImporteDesdeRequest(request);
            Orden ordenTrabajo = ordenRepository.findById(idOrdenTrabajo).get();

            Pago pago = new Pago();
            pago.setFechaPago(LocalDate.now());
            pago.setMedioPago(medioPago);
            pago.setEmpleado(empleado);
            pago.setImporte(importe);
            pago.setOrden(ordenTrabajo);

            pagoRepository.save(pago);
        }
    }

    private MedioPago obtenerMedioPagoDesdeRequest(HttpServletRequest request) {
        Long idMedioPago = Long.parseLong(request.getParameter("medioPago.id"));
        return medioPagoRepository.findById(idMedioPago).get();
    }

    private Empleado obtenerEmpleadoDesdeReequest(HttpServletRequest request) {
        return (Empleado) request.getSession().getAttribute("empleadoLogueado");
    }

    private int obtenerImporteDesdeRequest(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("abonado"));
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        pagoRepository.deleteById(id);
    }*/
}
