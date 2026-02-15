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
import java.util.ArrayList;

@Service
@Transactional
@AllArgsConstructor
public class PagoService {

    private final MedioPagoRepository medioPagoRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final OrdenService ordenService;
    private final OrdenRepository ordenRepository;
    private final PagoRepository pagoRepository;

    /**
     * Usado al crear una orden nueva o editarla desde el formulario dinámico.
     * Registra el pago inicial basado en el "abonado" que viene en el objeto orden.
     */
    public void guardar(Orden orden, Long idMedioPago) {
        if (orden.getAbonado() > 0) {
            MedioPago medioPago = medioPagoRepository.findById(idMedioPago)
                    .orElseThrow(() -> new RuntimeException("No existe el medio de pago"));

            Pago pago = new Pago();
            pago.setFechaPago(LocalDate.now());
            pago.setMedioPago(medioPago);
            pago.setImporte(orden.getAbonado());
            pago.setEmpleado(orden.getEmpleado());
            pago.setOrden(orden); // Seteamos la orden directamente

            pagoRepository.save(pago);
            ordenRepository.flush(); // Aseguramos que el pago esté en DB
        }

        actualizarSaldosYEstados(orden);
        // Nota: Aquí no hace falta ordenRepository.save porque suele venir de un flujo
        // que ya lo guarda, pero si ves que no actualiza el estado, agregalo.
    }

    /**
     * Usado desde el Kanban para cargar pagos parciales o totales de órdenes existentes.
     */
    public void registrarPagoExtra(Long ordenId, Integer monto, Long idMedioPago) {
        // 1. Buscamos la orden fresca de la DB
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        MedioPago medio = medioPagoRepository.findById(idMedioPago)
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        // 2. Creamos el pago y lo guardamos PRIMERO
        Pago nuevoPago = new Pago();
        nuevoPago.setImporte(monto);
        nuevoPago.setMedioPago(medio);
        nuevoPago.setFechaPago(LocalDate.now());
        nuevoPago.setEmpleado(orden.getEmpleado());
        nuevoPago.setOrden(orden);

        pagoRepository.save(nuevoPago);

        // 3. Forzamos un refresh de la orden para que Hibernate
        // traiga los pagos reales de la DB (incluyendo el nuevo) y no use los de memoria
        ordenRepository.flush();

        // 4. Recalculamos con los datos reales de la base
        actualizarSaldosYEstados(orden);

        ordenRepository.save(orden);
    }

    private void actualizarSaldosYEstados(Orden orden) {
        // IMPORTANTE: Pedimos los pagos directamente a la DB para no sumar duplicados de memoria
        Integer totalReal = pagoRepository.findByOrdenId(orden.getId()).stream()
                .mapToInt(Pago::getImporte)
                .sum();

        orden.setAbonado(totalReal);

        if (orden.getAbonado() >= orden.getTotal() && orden.getTotal() > 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(3L).get());
        } else if (orden.getAbonado() > 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(2L).get());
        } else {
            orden.setEstadoPago(estadoPagoRepository.findById(1L).get());
        }
    }
}