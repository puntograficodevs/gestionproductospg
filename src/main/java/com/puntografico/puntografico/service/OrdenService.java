package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service @Transactional
@AllArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final EstadoPagoRepository estadoPagoRepository;

    public Orden buscarPorId(Long id) {
        Assert.notNull(id, "Necesito que envíes un id para buscar.");

        return ordenRepository.findById(id).get();
    }

    public void guardar(Orden orden) {
        orden.setFechaPedido(LocalDate.now());
        orden.setEstadoOrden(estadoOrdenRepository.findById(1L).get());

        asignarEstadoPago(orden);

        if (orden.getItems() != null) {
            orden.getItems().forEach(item -> {
                item.setOrden(orden);
            });
        }

        if (orden.getPagos() != null) {
            orden.getPagos().removeIf(pago -> pago.getImporte() <= 0);
            orden.getPagos().forEach(pago -> pago.setOrden(orden));
        }

        ordenRepository.save(orden);
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
