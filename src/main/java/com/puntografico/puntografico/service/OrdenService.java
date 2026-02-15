package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
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

    public Orden buscarPorId(Long id) {
        Assert.notNull(id, "Necesito que envíes un id para buscar.");
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + id));
    }

    public void guardar(Orden orden) {
        // 1. CASO ORDEN NUEVA
        if (orden.getId() == null) {
            orden.setFechaPedido(LocalDate.now());
            // Estado inicial de producción: "Sin Hacer" (ID 1)
            orden.setEstadoOrden(estadoOrdenRepository.findById(1L).get());
        }
        // 2. CASO EDICIÓN O CORRECCIÓN
        else {
            Orden vieja = ordenRepository.findById(orden.getId())
                    .orElseThrow(() -> new RuntimeException("No se encontró la orden a editar"));

            // Preservamos datos que el formulario no maneja o no debe cambiar
            orden.setEmpleado(vieja.getEmpleado());
            orden.setFechaPedido(vieja.getFechaPedido());

            // Mantenemos el estado de pago actual; el PagoService lo actualizará
            // sumando los pagos reales de la DB después de esta llamada.
            orden.setEstadoPago(vieja.getEstadoPago());

            // Lógica de retorno desde estado "A Corregir"
            if (vieja.getEstadoOrden().getId() == 4L) {
                Integer previoId = vieja.getIdEstadoPrevio();
                Long estadoADondeVolver = (previoId != null) ? previoId.longValue() : 1L;

                orden.setEstadoOrden(estadoOrdenRepository.findById(estadoADondeVolver).get());

                // Limpiamos campos de la corrección ya procesada
                orden.setCorreccion(null);
                orden.setIdEstadoPrevio(null);
            } else {
                // Si no es corrección, mantenemos el estado de producción que ya tenía
                orden.setEstadoOrden(vieja.getEstadoOrden());
                orden.setIdEstadoPrevio(vieja.getIdEstadoPrevio());
            }
        }

        // Vinculamos los ítems a la orden para que JPA gestione la relación
        if (orden.getItems() != null) {
            orden.getItems().forEach(item -> item.setOrden(orden));
        }

        // Persistimos los cambios
        ordenRepository.save(orden);
    }

    @Transactional
    public void eliminar(Long id) {
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
        }
    }
}