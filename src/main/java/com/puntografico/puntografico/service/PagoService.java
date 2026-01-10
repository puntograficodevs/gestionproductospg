package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.Pago;
import com.puntografico.puntografico.repository.MedioPagoRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final MedioPagoRepository medioPagoRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public void guardar(HttpServletRequest request, Long idOrdenTrabajo) {
        MedioPago medioPago = obtenerMedioPagoDesdeRequest(request);
        Empleado empleado = obtenerEmpleadoDesdeReequest(request);
        int importe = obtenerImporteDesdeRequest(request);
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        Pago pago = new Pago();
        pago.setFechaPago(LocalDate.now());
        pago.setMedioPago(medioPago);
        pago.setEmpleado(empleado);
        pago.setImporte(importe);
        pago.setOrdenTrabajo(ordenTrabajo);

        pagoRepository.save(pago);
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
    }
}
