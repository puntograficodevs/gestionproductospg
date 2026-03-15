package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.EstadoOrden;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final ProductoRepository productoRepository;
    private final PagoService pagoService;
    private static final Long ID_ESTADO_SIN_HACER = 1L;
    private static final Long ID_ESTADO_CORRECCION = 4L;

    public Orden buscarPorId(Long id) {
        Assert.notNull(id, "El ID de la orden no puede venir nulo.");
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
            ordenNueva.setEstadoOrden(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER).get());
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

    public List<Orden> buscarPorIdNombreClienteOTelefono(@Param("dato") String dato, @Param("idRol") Long idRol) {
        return ordenRepository.buscarPorIdNombreClienteOTelefono(dato, idRol);
    }

    public List<Orden> buscarOrdenesConEstadoSegunRol(@Param("idEstado") Long idEstado, @Param("rolId") Long idRol) {
        return ordenRepository.buscarOrdenesConEstadoSegunRol(idEstado, idRol);
    }

    public void cambiarEstadoOrden(Long idOrden, Long idEstado) {
        Orden orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + idOrden));

        EstadoOrden nuevoEstado = estadoOrdenRepository.findById(idEstado)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + idEstado));

        orden.setEstadoOrden(nuevoEstado);
        ordenRepository.save(orden);
    }

    public void enviarAColumnaCorreccion(Long idOrden, String motivo) {
        Orden orden = buscarPorId(idOrden);
        orden.setIdEstadoPrevio(orden.getEstadoOrden().getId().intValue());
        EstadoOrden estadoCorregir = estadoOrdenRepository.findById(ID_ESTADO_CORRECCION).get();
        orden.setEstadoOrden(estadoCorregir);
        orden.setCorreccion(motivo);
        ordenRepository.save(orden);
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
            Long idEstadoOrdenPrevio = (ordenPersistida.getIdEstadoPrevio() != null) ? ordenPersistida.getIdEstadoPrevio() : ID_ESTADO_SIN_HACER;
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
        return Objects.equals(ordenPersistida.getEstadoOrden().getId(), ID_ESTADO_CORRECCION);
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