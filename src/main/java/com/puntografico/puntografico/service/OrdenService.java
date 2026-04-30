package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final ProductoRepository productoRepository;
    private final PagoService pagoService;
    private final MovimientoService movimientoService;
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
    public Orden guardar(Orden ordenNueva, Integer idProducto, Long idMedioPago, Empleado empleado) {
        String detalleRecibido = null;
        OrigenMovimiento origenMovimiento = OrigenMovimiento.FORMULARIO_CREACION;
        Orden orden = esProcesoCreacion(ordenNueva.getId())
                ? ordenNueva
                : buscarPorId(ordenNueva.getId());

        if (esProcesoCreacion(ordenNueva.getId())) {
            orden.setFechaPedido(LocalDate.now());
            orden.setEstadoOrden(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER).get());
            pagoService.crearPagoDesdeFormularioOrden(orden, idMedioPago);
            pagoService.actualizarEstadoPago(orden);
            vincularItems(orden, idProducto);
        } else {
            detalleRecibido = agregarDetalleMovimiento(orden, ordenNueva);
            origenMovimiento = modificarOrigenMovimiento(orden);
            orden.setNombreCliente(ordenNueva.getNombreCliente());
            orden.setTelefonoCliente(ordenNueva.getTelefonoCliente());
            orden.setEsCuentaCorriente(ordenNueva.isEsCuentaCorriente());
            orden.setFechaMuestra(ordenNueva.getFechaMuestra());
            orden.setFechaEntrega(ordenNueva.getFechaEntrega());
            orden.setHoraEntrega(ordenNueva.getHoraEntrega());
            orden.setNecesitaFactura(ordenNueva.isNecesitaFactura());
            modificarPagosSegunCorresponda(orden, ordenNueva, idMedioPago);
            modificarEstadoOrdenSiCorreccion(orden);
            reemplazarItems(orden, ordenNueva, idProducto);
        }

        Movimiento movimiento = movimientoService.registrar(null, empleado, detalleRecibido, origenMovimiento);
        orden.agregarMovimiento(movimiento);
        return ordenRepository.save(orden);
    }

    private boolean esProcesoCreacion(Long idOrden) {
        return idOrden == null;
    }

    private boolean esProcesoCorreccion(Orden ordenPersistida) {
        return Objects.equals(ordenPersistida.getEstadoOrden().getId(), ID_ESTADO_CORRECCION);
    }

    private void modificarPagosSegunCorresponda(Orden ordenOriginal, Orden ordenModificada, Long idMedioPago) {
        boolean cambioAbonado = esImporteAbonadoDistinto(ordenOriginal, ordenModificada);

        ordenOriginal.setTotal(ordenModificada.getTotal());
        ordenOriginal.setSubtotal(ordenModificada.getSubtotal());
        ordenOriginal.setPrecioDisenio(ordenModificada.getPrecioDisenio());
        ordenOriginal.setAbonado(ordenModificada.getAbonado());

        if (cambioAbonado) {
            pagoService.eliminarPagosAsociados(ordenOriginal.getId());
            pagoService.crearPagoDesdeFormularioOrden(ordenOriginal, idMedioPago);
        }

        pagoService.actualizarEstadoPago(ordenOriginal);
    }

    private boolean esImporteAbonadoDistinto(Orden ordenPersistida, Orden ordenNueva) {
        return ordenPersistida.getAbonado() != ordenNueva.getAbonado();
    }

    private void modificarEstadoOrdenSiCorreccion(Orden ordenPersistida) {
        if (esProcesoCorreccion(ordenPersistida)) {
            Long idEstadoOrdenPrevio = (ordenPersistida.getIdEstadoPrevio() != null) ? ordenPersistida.getIdEstadoPrevio() : ID_ESTADO_SIN_HACER;
            ordenPersistida.setEstadoOrden(estadoOrdenRepository.findById(idEstadoOrdenPrevio).get());
            ordenPersistida.setCorreccion(null);
            ordenPersistida.setIdEstadoPrevio(null);
        }
    }

    private String agregarDetalleMovimiento(Orden ordenPersistida, Orden ordenModificada) {
        String detalle = esProcesoCorreccion(ordenPersistida)
                ? "El pedido de corrección era: " + ordenPersistida.getCorreccion()  + "."
                : null;

        if (esImporteAbonadoDistinto(ordenPersistida, ordenModificada)) {
            detalle = (esCampoVacio(detalle))
                    ? "Se modifica abonado a: $" + ordenModificada.getAbonado()
                    : detalle + " | Se modifica abonado a: $" + ordenModificada.getAbonado();
        }

        return detalle;
    }

    private boolean esCampoVacio(String detalle) {
        return detalle == null || detalle.isBlank();
    }

    private OrigenMovimiento modificarOrigenMovimiento(Orden ordenPersistida) {
        return esProcesoCorreccion(ordenPersistida)
                ? OrigenMovimiento.FORMULARIO_CORRECCION
                : OrigenMovimiento.FORMULARIO_EDICION;
    }

    private void vincularItems(Orden orden, Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (orden.getItems() != null) {
            orden.getItems().forEach(item -> {
                item.setOrden(orden);
                item.setProducto(producto);
            });
        }
    }

    private void reemplazarItems(Orden orden, Orden ordenNueva, Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        orden.getItems().clear();

        if (ordenNueva.getItems() != null) {
            ordenNueva.getItems().forEach(item -> {
                item.setOrden(orden);
                item.setProducto(producto);
                orden.getItems().add(item);
            });
        }
    }

    public List<Orden> buscarPorIdNombreClienteOTelefono(@Param("dato") String dato, @Param("idRol") Long idRol) {
        return ordenRepository.buscarPorIdNombreClienteOTelefono(dato, idRol);
    }

    public List<Orden> buscarOrdenesConEstadoSegunRol(@Param("idEstado") Long idEstado, @Param("rolId") Long idRol) {
        return ordenRepository.buscarOrdenesConEstadoSegunRol(idEstado, idRol);
    }

    public void cambiarEstadoOrden(Long idOrden, Long idNuevoEstado, boolean asignarEncargado, Empleado empleadoLogueado) {
        Orden orden = buscarPorId(idOrden);

        EstadoOrden nuevoEstado = estadoOrdenRepository.findById(idNuevoEstado)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + idNuevoEstado));

        asignarEncargadoSiCorresponde(idNuevoEstado, orden, asignarEncargado, empleadoLogueado);
        orden.setEstadoOrden(nuevoEstado);
        Movimiento movimiento = movimientoService.registrar(idNuevoEstado, empleadoLogueado, null, OrigenMovimiento.CAMBIO_ESTADO);
        orden.agregarMovimiento(movimiento);
        ordenRepository.save(orden);
    }

    private void asignarEncargadoSiCorresponde(Long idEstado, Orden orden, boolean asignarEncargado, Empleado empleadoLogueado) {
        if (Objects.equals(idEstado, 2L) && asignarEncargado) {
            orden.setEncargadoProduccion(empleadoLogueado);
        }
    }

    public void enviarAColumnaCorreccion(Long idOrden, String motivo, Empleado empleado) {
        Orden orden = buscarPorId(idOrden);
        orden.setIdEstadoPrevio(orden.getEstadoOrden().getId().intValue());
        EstadoOrden estadoCorregir = estadoOrdenRepository.findById(ID_ESTADO_CORRECCION).get();
        orden.setEstadoOrden(estadoCorregir);
        orden.setCorreccion(motivo);
        Movimiento movimiento = movimientoService.registrar(ID_ESTADO_CORRECCION, empleado, motivo, OrigenMovimiento.PEDIDO_CORRECCION);
        orden.agregarMovimiento(movimiento);
        ordenRepository.save(orden);
    }

    public List<Orden> obtenerOrdenesPorSemana(LocalDate lunes, String tipo, Empleado empleado) {
        LocalDate domingo = lunes.plusDays(6);
        List<Orden> ordenesPorSemanaOrdenadas = ordenRepository.buscarPorSemanaYTipo(lunes, domingo, tipo, empleado.getRol().getId());
        return ordenarYFormatear(ordenesPorSemanaOrdenadas, empleado);
    }

    public List<Orden> ordenarYFormatear(List<Orden> ordenes, Empleado empleado) {
        return ordenes.stream()
                .sorted(obtenerCriterioOrdenamiento(empleado))
                .collect(Collectors.toList());
    }

    private Comparator<Orden> obtenerCriterioOrdenamiento(Empleado empleado) {
        if ("maricommunity".equalsIgnoreCase(empleado.getUsername())) {
            return Comparator.comparing(Orden::getId).reversed();
        }

        return Comparator.comparing(Orden::getFechaEntrega)
                .thenComparing(orden -> parsearHora(orden.getHoraEntrega()));
    }

    private LocalTime parsearHora(String horaEntrega) {
        if (esHoraEntregaInvalida(horaEntrega)) {
            return LocalTime.MAX;
        }

        try {
            String horaEntregaFormateada = formatearHoraEntrega(horaEntrega);
            return construirLocalTime(horaEntregaFormateada);
        } catch (Exception e) {
            return LocalTime.MAX;
        }
    }

    private boolean esHoraEntregaInvalida(String hora) {
        return hora == null || hora.trim().isEmpty();
    }

    private String formatearHoraEntrega(String horaEntrega) {
        return horaEntrega.replace(":", "").replace(".", "").trim();
    }

    private LocalTime construirLocalTime(String horaEntregaFormateada) {
        int horas;
        int minutos = 0;

        if (horaEntregaFormateada.length() <= 2) {
            horas = Integer.parseInt(horaEntregaFormateada);
        } else if (horaEntregaFormateada.length() == 3) {
            horas = Integer.parseInt(horaEntregaFormateada.substring(0, 1));
            minutos = Integer.parseInt(horaEntregaFormateada.substring(1));
        } else {
            horas = Integer.parseInt(horaEntregaFormateada.substring(0, 2));
            minutos = Integer.parseInt(horaEntregaFormateada.substring(2));
        }

        return LocalTime.of(normalizarHora(horas), normalizarMinutos(minutos));
    }

    private int normalizarHora(int horas) {
        return Math.min(horas, 23);
    }

    private int normalizarMinutos(int minutos) {
        return Math.min(minutos, 59);
    }
}