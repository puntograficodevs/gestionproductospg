package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PagoService {

    private final EstadoPagoRepository estadoPagoRepository;
    private final OrdenRepository ordenRepository;
    private final PagoRepository pagoRepository;
    private final MedioPagoService medioPagoService;
    private static final Long ID_ESTADO_SIN_PAGAR = 1L;
    private static final Long ID_ESTADO_SENIADO = 2L;
    private static final Long ID_ESTADO_PAGADO = 3L;

    public void registrarPagoExtra(Long idOrden, Integer montoAbonado, Long idMedioPago) {
        Orden orden = ordenRepository.findById(idOrden).get();
        MedioPago medioPago = medioPagoService.buscarPorId(idMedioPago);
        Pago pago = new Pago();

        pago.setImporte(montoAbonado);
        pago.setMedioPago(medioPago);
        pago.setFechaPago(LocalDate.now());
        pago.setOrden(orden);
        pago.setEmpleado(orden.getEmpleado());
        pagoRepository.save(pago);
        pagoRepository.flush();
        orden.setAbonado(calcularTotalAbonado(orden));
        actualizarEstadoPago(orden);
        ordenRepository.save(orden);
    }

    public void crearPagoDesdeFormularioOrden(Orden orden, Long idMedioPago) {
        if (orden.getAbonado() > 0 ) {
            Pago pago = new Pago();
            pago.setImporte(orden.getAbonado());
            pago.setMedioPago(medioPagoService.buscarPorId(idMedioPago));
            pago.setFechaPago(orden.getFechaPedido());
            pago.setOrden(orden);
            pago.setEmpleado(orden.getEmpleado());
            orden.getPagos().add(pago);
        }
    }

    public void actualizarEstadoPago(Orden orden) {
        int totalAbonado = calcularTotalAbonado(orden);
        int pagoRestante = orden.getTotal() - totalAbonado;

        if (totalAbonado == 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(ID_ESTADO_SIN_PAGAR).get());
        } else if (pagoRestante != 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(ID_ESTADO_SENIADO).get());
        } else {
            orden.setEstadoPago(estadoPagoRepository.findById(ID_ESTADO_PAGADO).get());
        }
    }

    public void eliminarPagosAsociados(Long idOrden) {
        List<Pago> pagosAsociados = pagoRepository.findByOrdenId(idOrden);
        Orden orden = ordenRepository.findById(idOrden).get();

        for (Pago pago : pagosAsociados) {
            pagoRepository.deleteById(pago.getId());
        }

        orden.getPagos().clear();
    }

    private int calcularTotalAbonado(Orden orden) {
        int totalAbonado = 0;

        if (!orden.getPagos().isEmpty()) {
            for (Pago pago : orden.getPagos()) {
                totalAbonado += pago.getImporte();
            }
        }

        return totalAbonado;
    }
}