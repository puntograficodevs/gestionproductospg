package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service @Transactional
@AllArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final EstadoPagoRepository estadoPagoRepository;

    public Orden guardar(Orden orden) {
        orden.setFechaPedido(LocalDate.now());
        orden.setEstadoOrden(estadoOrdenRepository.findById(1L).orElseThrow(
                () -> new RuntimeException("Estado inicial no encontrado.")));

        asignarEstadoPago(orden);

        if (orden.getItems() != null) {
            orden.getItems().forEach(ordenItem -> ordenItem.setOrden(orden));
        }

        if (orden.getPagos() != null) {
            orden.getPagos().removeIf(pago -> pago.getImporte() <= 0);
            orden.getPagos().forEach(pago -> {
                pago.setOrden(orden);
                pago.setFechaPago(LocalDate.now());
            });
        }

        return ordenRepository.save(orden);
    }

    private void asignarEstadoPago(Orden orden) {
        int resta = orden.getTotal() - orden.getAbonado();

        if (resta == 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(3L).get()); // PAGADO
        } else if (orden.getAbonado() == 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(1L).get()); // PENDIENTE
        } else {
            orden.setEstadoPago(estadoPagoRepository.findById(2L).get()); // SEÑA
        }
    }
}
