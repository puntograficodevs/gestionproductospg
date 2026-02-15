package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.MedioPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
@AllArgsConstructor
public class PagoService {

    private final MedioPagoRepository medioPagoRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final OrdenRepository ordenRepository;
    private final PagoRepository pagoRepository;

    /**
     * Procesa el pago inicial desde el formulario de Orden (Creación o Edición).
     * Si la orden ya existe, busca el primer pago y lo actualiza (sobreescritura).
     */
    public void guardar(Orden orden, Long idMedioPago) {
        MedioPago medioPago = medioPagoRepository.findById(idMedioPago)
                .orElseThrow(() -> new RuntimeException("No existe el medio de pago"));

        // 1. Limpieza total de pagos previos de esa orden
        if (orden.getId() != null) {
            pagoRepository.deleteByOrdenId(orden.getId());
            pagoRepository.flush();
        }

        // 2. Si hay abono, se crea el pago único
        if (orden.getAbonado() > 0) {
            crearNuevoPago(orden, orden.getAbonado(), medioPago);
            pagoRepository.flush();
        }

        // 3. Recalculamos la suma y asignamos el Estado (ID 1, 2 o 3)
        actualizarSaldosYEstados(orden);
    }

    /**
     * Registra pagos adicionales (Pagos Extra) desde el Kanban u otras vistas.
     * Estos pagos no pisan al pago inicial.
     */
    public void registrarPagoExtra(Long ordenId, Integer monto, Long idMedioPago) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        MedioPago medio = medioPagoRepository.findById(idMedioPago)
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        crearNuevoPago(orden, monto, medio);
        pagoRepository.flush();

        actualizarSaldosYEstados(orden);
        // Guardamos la orden para persistir el nuevo estado de pago calculado
        ordenRepository.save(orden);
    }

    /**
     * Suma todos los pagos asociados a la orden y asigna el Estado de Pago correspondiente.
     */
    public void actualizarSaldosYEstados(Orden orden) {
        if (orden.getId() == null) return;

        // Sumatoria real de todos los tickets de pago en la base de datos
        Integer totalRealAbonado = pagoRepository.findByOrdenId(orden.getId()).stream()
                .mapToInt(Pago::getImporte)
                .sum();

        orden.setAbonado(totalRealAbonado);

        // Determinación del Estado de Pago (1: Pendiente, 2: Seña/Parcial, 3: Pagado)
        if (orden.getAbonado() >= orden.getTotal() && orden.getTotal() > 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(3L).get());
        } else if (orden.getAbonado() > 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(2L).get());
        } else {
            orden.setEstadoPago(estadoPagoRepository.findById(1L).get());
        }
    }

    private void crearNuevoPago(Orden orden, Integer monto, MedioPago medio) {
        Pago pago = new Pago();
        pago.setFechaPago(LocalDate.now());
        pago.setMedioPago(medio);
        pago.setImporte(monto);
        pago.setEmpleado(orden.getEmpleado());
        pago.setOrden(orden);
        pagoRepository.save(pago);
    }
}