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
import java.time.LocalDateTime;
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
    public Orden guardar(Orden ordenNueva, Integer idProducto, Long idMedioPago, Empleado empleadoLogueado) {
        Movimiento movimiento;

        if (esProcesoCreacion(ordenNueva.getId())) {
            ordenNueva.setFechaPedido(LocalDate.now());
            ordenNueva.setEstadoOrden(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER).get());

            if (ordenNueva.getAbonado() > 0) {
                pagoService.crearPagoDesdeFormularioOrden(ordenNueva, idMedioPago);
            }

            pagoService.actualizarEstadoPago(ordenNueva);

            movimiento = crearMovimientoCreacion(ordenNueva, empleadoLogueado);

        } else {
            Orden ordenPersistida = buscarPorId(ordenNueva.getId());

            movimiento = crearMovimientoEdicionOCorreccion(ordenPersistida, ordenNueva, empleadoLogueado);

            ordenNueva.setFechaPedido(ordenPersistida.getFechaPedido());
            ordenNueva.setEmpleado(ordenPersistida.getEmpleado());
            ordenNueva.setMovimientos(ordenPersistida.getMovimientos());

            asignarEstadoOrdenSegunProceso(ordenPersistida, ordenNueva);
            asignarEncargadoOrdenSiCorresponde(ordenPersistida, ordenNueva);
            asignarPagosSegunModificacionAbonado(ordenPersistida, ordenNueva, idMedioPago);
            limpiarItemsParaEvitarDuplicacion(ordenPersistida);
        }

        vincularItemsSiCorresponde(ordenNueva, idProducto);
        ordenNueva.agregarMovimiento(movimiento);

        return ordenRepository.save(ordenNueva);
    }

    private Movimiento crearMovimientoCreacion(Orden orden, Empleado empleadoLogueado) {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.TOMAR_PEDIDO);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setEmpleado(empleadoLogueado);
        return movimiento;
    }

    private Movimiento crearMovimientoEdicionOCorreccion(
            Orden ordenPersistida,
            Orden ordenNueva,
            Empleado empleadoLogueado
    ) {
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setEmpleado(empleadoLogueado);

        if (esProcesoCorreccion(ordenPersistida)) {
            movimiento.setTipoMovimiento(TipoMovimiento.CORREGIR_ORDEN);
            agregarDetalleMovimiento(
                    movimiento,
                    "El pedido de corrección era: " + ordenPersistida.getCorreccion()
            );
        } else {
            movimiento.setTipoMovimiento(TipoMovimiento.EDITAR_ORDEN);
        }

        if (esImporteAbonadoDistinto(ordenPersistida, ordenNueva)) {
            agregarDetalleMovimiento(
                    movimiento,
                    "Se modifica abonado a: $" + ordenNueva.getAbonado()
            );
        }

        return movimiento;
    }

    private void agregarDetalleMovimiento(Movimiento movimiento, String detalleNuevo) {
        if (movimiento.getDetalle() == null || movimiento.getDetalle().isBlank()) {
            movimiento.setDetalle(detalleNuevo);
        } else {
            movimiento.setDetalle(movimiento.getDetalle() + " | " + detalleNuevo);
        }
    }

    public List<Orden> buscarPorIdNombreClienteOTelefono(@Param("dato") String dato, @Param("idRol") Long idRol) {
        return ordenRepository.buscarPorIdNombreClienteOTelefono(dato, idRol);
    }

    public List<Orden> buscarOrdenesConEstadoSegunRol(@Param("idEstado") Long idEstado, @Param("rolId") Long idRol) {
        return ordenRepository.buscarOrdenesConEstadoSegunRol(idEstado, idRol);
    }

    public void cambiarEstadoOrden(Long idOrden, Long idEstado, boolean asignarEncargado, Empleado empleadoLogueado) {
        Orden orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + idOrden));

        EstadoOrden nuevoEstado = estadoOrdenRepository.findById(idEstado)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + idEstado));

        asignarEncargadoSiCorresponde(idEstado, orden, asignarEncargado, empleadoLogueado);
        orden.setEstadoOrden(nuevoEstado);
        ordenRepository.save(orden);
    }

    private void asignarEncargadoSiCorresponde(Long idEstado, Orden orden, boolean asignarEncargado, Empleado empleadoLogueado) {
        if (idEstado == 2 && asignarEncargado) {
            orden.setEncargadoProduccion(empleadoLogueado);
        }
    }

    public void enviarAColumnaCorreccion(Long idOrden, String motivo) {
        Orden orden = buscarPorId(idOrden);
        orden.setIdEstadoPrevio(orden.getEstadoOrden().getId().intValue());
        EstadoOrden estadoCorregir = estadoOrdenRepository.findById(ID_ESTADO_CORRECCION).get();
        orden.setEstadoOrden(estadoCorregir);
        orden.setCorreccion(motivo);
        ordenRepository.save(orden);
    }

    public List<Orden> obtenerOrdenesPorSemana(LocalDate lunes, String tipo, Empleado empleado) {
        LocalDate domingo = lunes.plusDays(6);
        List<Orden> ordenesPorSemanaOrdenadas = ordenRepository.buscarPorSemanaYTipo(lunes, domingo, tipo, empleado.getRol().getId());
        return ordenarYFormatear(ordenesPorSemanaOrdenadas, empleado);
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

    private void asignarEncargadoOrdenSiCorresponde(Orden ordenPersistida, Orden ordenNueva) {
        if (ordenPersistida.getEncargadoProduccion() != null) {
            ordenNueva.setEncargadoProduccion(ordenPersistida.getEncargadoProduccion());
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