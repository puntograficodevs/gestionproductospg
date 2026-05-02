package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    public Movimiento registrar(Long nuevoEstadoId, Empleado empleado, String detalleRecibido, OrigenMovimiento origen) {
        TipoMovimiento tipoMovimiento = obtenerTipoMovimiento(nuevoEstadoId, origen);
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setDetalle(obtenerDetalleSegunMovimiento(tipoMovimiento, detalleRecibido));
        movimiento.setEmpleado(empleado);
        movimiento.setFecha(LocalDateTime.now());
        return movimiento;
    }

    private TipoMovimiento obtenerTipoMovimiento(Long nuevoEstadoId, OrigenMovimiento origen) {
        if (OrigenMovimiento.CAMBIO_ESTADO.equals(origen)) {
            return obtenerTipoMovimientoSegunEstado(nuevoEstadoId);
        } else if (OrigenMovimiento.REGISTRO_PAGO.equals(origen)) {
            return TipoMovimiento.REGISTRAR_PAGO;
        } else if (OrigenMovimiento.FORMULARIO_CREACION.equals(origen)) {
            return TipoMovimiento.TOMAR_PEDIDO;
        } else if (OrigenMovimiento.FORMULARIO_EDICION.equals(origen)) {
            return TipoMovimiento.EDITAR_ORDEN;
        } else if (OrigenMovimiento.FORMULARIO_CORRECCION.equals(origen)) {
            return TipoMovimiento.CORREGIR_ORDEN;
        } else if (OrigenMovimiento.PEDIDO_CORRECCION.equals(origen)) {
            return TipoMovimiento.PEDIR_CORRECCION;
        }
        throw new IllegalArgumentException("Este movimiento no debería registrarse desde donde se hace.");
    }

    private TipoMovimiento obtenerTipoMovimientoSegunEstado(Long nuevoEstadoId) {
        if (Objects.equals(nuevoEstadoId, 1L)) {
            return TipoMovimiento.VOLVER_A_SIN_HACER;
        }
        if (Objects.equals(nuevoEstadoId, 2L)) {
            return TipoMovimiento.PASAR_A_EN_PROCESO;
        }
        if (Objects.equals(nuevoEstadoId, 3L)) {
            return TipoMovimiento.MARCAR_LISTA_PARA_RETIRAR;
        }
        if (Objects.equals(nuevoEstadoId, 5L)) {
            return TipoMovimiento.MARCAR_PEDIDO_COMO_RETIRADO;
        }

        throw new IllegalArgumentException("Estado no contemplado para movimiento: " + nuevoEstadoId);
    }

    private String obtenerDetalleSegunMovimiento(TipoMovimiento tipoMovimiento, String detalleRecibido) {
        if (TipoMovimiento.REGISTRAR_PAGO.equals(tipoMovimiento)) {
            return "Se registró pago de $" + detalleRecibido;
        } else if (TipoMovimiento.PEDIR_CORRECCION.equals(tipoMovimiento)) {
            return "Se pidió una corrección: " + detalleRecibido;
        }
        return detalleRecibido;
    }

    public List<Movimiento> buscarPorOrden(Long idOrden) {
        return movimientoRepository.findByOrdenIdOrderByFechaDesc(idOrden);
    }
}
