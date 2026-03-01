package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
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
    private final PagoService pagoService;

    public Orden buscarPorId(Long id) {
        Assert.notNull(id, "Necesito que envíes un id para buscar.");
        return ordenRepository.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + id));
    }

    @Transactional
    public void eliminar(Long id) {
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
        }
    }

    @Transactional
    public Orden guardar(Orden ordenNueva, Integer idProducto, Long idMedioPago) {
        if (esProcesoCreacion(ordenNueva.getId())) {
            ordenNueva.setFechaPedido(LocalDate.now());
            ordenNueva.setEstadoOrden(estadoOrdenRepository.findById(1L).get());
            if (ordenNueva.getAbonado() > 0) pagoService.crearPagoDesdeFormularioOrden(ordenNueva, idMedioPago);
            pagoService.actualizarEstadoPago(ordenNueva);
        } else {
            Orden ordenPersistida = buscarPorId(ordenNueva.getId());
            ordenNueva.setFechaPedido(ordenPersistida.getFechaPedido());
            ordenNueva.setEmpleado(ordenPersistida.getEmpleado());
            asignarEstadoOrdenSegunProceso(ordenPersistida, ordenNueva);
            asignarPagosSegunModificacionAbonado(ordenPersistida, ordenNueva, idMedioPago);
            limpiarItemsParaEvitarDuplicacion(ordenPersistida);
        }

        vincularItemsSiCorresponde(ordenNueva, idProducto);
        return ordenRepository.save(ordenNueva);
    }

    private void asignarPagosSegunModificacionAbonado(Orden ordenPersistida, Orden ordenNueva, Long idMedioPago) {
        if (esImporteAbonadoDistinto(ordenPersistida, ordenNueva)) {
            pagoService.eliminarPagosAsociados(ordenNueva.getId());
            pagoService.crearPagoDesdeFormularioOrden(ordenNueva, idMedioPago);
        } else {
            ordenNueva.setPagos(ordenPersistida.getPagos());
        }

        pagoService.actualizarEstadoPago(ordenNueva);
    }

    private boolean esImporteAbonadoDistinto(Orden ordenPersistida, Orden ordenNueva) {
        return ordenPersistida.getAbonado() != ordenNueva.getAbonado();
    }

    private void limpiarItemsParaEvitarDuplicacion(Orden ordenPersistida) {
        ordenPersistida.getItems().clear();
        ordenRepository.saveAndFlush(ordenPersistida);
    }

    private void asignarEstadoOrdenSegunProceso(Orden ordenPersistida, Orden ordenNueva) {
        if (esProcesoCorreccion(ordenPersistida)) {
            Long idEstadoOrdenPrevio = (ordenPersistida.getIdEstadoPrevio() != null) ? ordenPersistida.getIdEstadoPrevio() : 1L;
            ordenNueva.setEstadoOrden(estadoOrdenRepository.findById(idEstadoOrdenPrevio).get());
            ordenNueva.setCorreccion(null);
            ordenNueva.setIdEstadoPrevio(null);
        } else {
            ordenNueva.setEstadoOrden(ordenPersistida.getEstadoOrden());
        }
    }

    private boolean esProcesoCreacion(Long idOrden) {
        return idOrden == null;
    }

    private boolean esProcesoCorreccion(Orden ordenPersistida) {
        return ordenPersistida.getEstadoOrden().getId() == 4L;
    }

    private void vincularItemsSiCorresponde(Orden orden, Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (orden.getItems() != null) {
            orden.getItems().forEach(item -> {
                item.setOrden(orden);
                item.setProducto(producto);
            });
        }
    }
}