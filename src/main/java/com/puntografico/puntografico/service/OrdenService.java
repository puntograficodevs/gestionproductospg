package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.Pago;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
@AllArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final ProductoRepository productoRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final MedioPagoService medioPagoService;

    public Orden buscarPorId(Long id) {
        Assert.notNull(id, "Necesito que envíes un id para buscar.");
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + id));
    }

    public Orden guardar(Orden orden, Integer productoId, Long idMedioPago) {
        Producto productoBase = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (orden.getId() == null) {
            // --- CASO NUEVA ---
            orden.setFechaPedido(LocalDate.now());
            orden.setEstadoOrden(estadoOrdenRepository.findById(1L).get());
            actualizarEstadoPagoSegunMonto(orden);
            if (orden.getAbonado() > 0) {
                registrarPagoInicial(orden, idMedioPago);
            }
        } else {
            // --- CASO EDICIÓN ---
            Orden vieja = buscarPorId(orden.getId());

            // 1. Preservamos datos originales
            orden.setEmpleado(vieja.getEmpleado());
            orden.setFechaPedido(vieja.getFechaPedido());
            orden.setPagos(vieja.getPagos());

            // 2. Sincronizamos el Estado de Pago (Punto 2: Que no cambie si no debe)
            // Solo recalculamos si el abonado cambió significativamente,
            // sino mantenemos el que tenía la vieja.
            orden.setEstadoPago(vieja.getEstadoPago());

            // 3. Lógica de corrección
            if (vieja.getEstadoOrden().getId() == 4L) {
                Long volverA = (vieja.getIdEstadoPrevio() != null) ? vieja.getIdEstadoPrevio().longValue() : 1L;
                orden.setEstadoOrden(estadoOrdenRepository.findById(volverA).get());
                orden.setCorreccion(null);
                orden.setIdEstadoPrevio(null);
            } else {
                orden.setEstadoOrden(vieja.getEstadoOrden());
            }

            // 4. ¡IMPORTANTE! Limpiamos los ítems viejos de la base de datos
            // para que la cascada de los "nuevos" que vienen del form no duplique.
            vieja.getItems().clear();
            ordenRepository.saveAndFlush(vieja);
        }

        // --- VINCULACIÓN DE ÍTEMS (Punto 1 - Cascada) ---
        if (orden.getItems() != null) {
            orden.getItems().forEach(item -> {
                item.setOrden(orden);
                item.setProducto(productoBase);
            });
        }

        return ordenRepository.save(orden);
    }

    private void actualizarEstadoPagoSegunMonto(Orden orden) {
        if (orden.getAbonado() >= orden.getTotal()) {
            orden.setEstadoPago(estadoPagoRepository.findById(3L).get()); // Pagado
        } else if (orden.getAbonado() > 0) {
            orden.setEstadoPago(estadoPagoRepository.findById(2L).get()); // Señado
        } else {
            orden.setEstadoPago(estadoPagoRepository.findById(1L).get()); // Impago
        }
    }

    private void registrarPagoInicial(Orden orden, Long idMedioPago) {
        Pago pago = new Pago();
        pago.setImporte(orden.getAbonado());
        pago.setFechaPago(LocalDate.now());
        pago.setMedioPago(medioPagoService.buscarPorId(idMedioPago));
        pago.setOrden(orden);
        pago.setEmpleado(orden.getEmpleado());
        orden.getPagos().add(pago);
    }

    @Transactional
    public void eliminar(Long id) {
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
        }
    }
}