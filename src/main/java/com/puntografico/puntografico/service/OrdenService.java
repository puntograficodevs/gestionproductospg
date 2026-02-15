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
        // 1. CASO ORDEN NUEVA
        if (orden.getId() == null) {
            orden.setFechaPedido(LocalDate.now());
            orden.setEstadoOrden(estadoOrdenRepository.findById(1L).get()); // 1L = Sin Hacer
        }
        // 2. CASO EDICIÓN O CORRECCIÓN
        else {
            // Recuperamos la orden tal cual está en la DB antes de que los datos del form la pisen
            Orden vieja = ordenRepository.findById(orden.getId())
                    .orElseThrow(() -> new RuntimeException("No se encontró la orden a editar"));

            // REGLA A: El EMPLEADO no se modifica (se mantiene el original)
            orden.setEmpleado(vieja.getEmpleado());

            // REGLA B: Mantenemos la fecha original
            orden.setFechaPedido(vieja.getFechaPedido());

            // REGLA C: Lógica de Corrección vs Edición Común
            if (vieja.getEstadoOrden().getId() == 4L) {
                // Estaba en estado CORREGIR, hay que restaurarla
                Integer previoId = vieja.getIdEstadoPrevio();
                Long estadoADondeVolver = (previoId != null) ? previoId.longValue() : 1L;

                orden.setEstadoOrden(estadoOrdenRepository.findById(estadoADondeVolver).get());

                // Limpiamos los campos de corrección porque ya se cumplió
                orden.setCorreccion(null);
                orden.setIdEstadoPrevio(null);
            } else {
                // Es una edición común, mantenemos el estado que ya tenía
                orden.setEstadoOrden(vieja.getEstadoOrden());
                // Por las dudas, mantenemos el estado previo si es que existía
                orden.setIdEstadoPrevio(vieja.getIdEstadoPrevio());
            }
        }

        // 3. LÓGICA DE PAGOS E ÍTEMS (Se mantiene igual)
        asignarEstadoPago(orden);

        if (orden.getItems() != null) {
            orden.getItems().forEach(item -> item.setOrden(orden));
        }

        if (orden.getPagos() != null) {
            orden.getPagos().removeIf(pago -> pago.getImporte() <= 0);
            orden.getPagos().forEach(pago -> pago.setOrden(orden));
        }

        ordenRepository.save(orden);
    }

    @Transactional
    public void eliminar(Long id) {
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
        }
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
