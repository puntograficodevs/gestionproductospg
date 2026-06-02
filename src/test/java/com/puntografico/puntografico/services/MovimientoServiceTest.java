package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.domain.OrigenMovimiento;
import com.puntografico.puntografico.domain.TipoMovimiento;
import com.puntografico.puntografico.repository.MovimientoRepository;
import com.puntografico.puntografico.service.MovimientoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MovimientoService.class)
class MovimientoServiceTest {

    @Autowired
    private MovimientoService movimientoService;

    @MockBean
    private MovimientoRepository movimientoRepository;

    @ParameterizedTest(name = "{0}")
    @MethodSource("movimientosPorCambioDeEstado")
    void registrar_conCambioDeEstadoContemplado_creaMovimientoConTipoEsperado(
            String caso,
            Long nuevoEstadoId,
            TipoMovimiento tipoEsperado
    ) {
        Empleado empleado = empleado();

        Movimiento resultado = movimientoService.registrar(nuevoEstadoId, empleado, "detalle", OrigenMovimiento.CAMBIO_ESTADO);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(tipoEsperado);
        assertThat(resultado.getDetalle()).isEqualTo("detalle");
        assertThat(resultado.getEmpleado()).isSameAs(empleado);
        assertThat(resultado.getFecha()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("movimientosPorOrigenFormulario")
    void registrar_conOrigenDeFormulario_creaMovimientoConTipoYDetalleRecibido(
            String caso,
            OrigenMovimiento origen,
            TipoMovimiento tipoEsperado
    ) {
        Empleado empleado = empleado();

        Movimiento resultado = movimientoService.registrar(null, empleado, "detalle", origen);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(tipoEsperado);
        assertThat(resultado.getDetalle()).isEqualTo("detalle");
        assertThat(resultado.getEmpleado()).isSameAs(empleado);
        assertThat(resultado.getFecha()).isNotNull();
    }

    @Test
    void registrar_conRegistroPago_formateaDetalleConImporte() {
        Movimiento resultado = movimientoService.registrar(null, empleado(), "1000", OrigenMovimiento.REGISTRO_PAGO);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(TipoMovimiento.REGISTRAR_PAGO);
        assertThat(resultado.getDetalle()).isEqualTo("Se registró pago de $1000");
    }

    @Test
    void registrar_conPedidoCorreccion_formateaDetalleConMotivo() {
        Movimiento resultado = movimientoService.registrar(null, empleado(), "Cambiar tamaño", OrigenMovimiento.PEDIDO_CORRECCION);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(TipoMovimiento.PEDIR_CORRECCION);
        assertThat(resultado.getDetalle()).isEqualTo("Se pidió una corrección: Cambiar tamaño");
    }

    @Test
    void registrar_conOrigenNoContemplado_lanzaExcepcion() {
        assertThatThrownBy(() -> movimientoService.registrar(null, empleado(), "detalle", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Este movimiento no debería registrarse desde donde se hace.");
    }

    @Test
    void registrar_conCambioEstadoSinEstado_lanzaExcepcion() {
        assertThatThrownBy(() -> movimientoService.registrar(null, empleado(), "detalle", OrigenMovimiento.CAMBIO_ESTADO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Estado no contemplado para movimiento: null");
    }

    @Test
    void registrar_conCambioEstadoNoContemplado_lanzaExcepcion() {
        assertThatThrownBy(() -> movimientoService.registrar(4L, empleado(), "detalle", OrigenMovimiento.CAMBIO_ESTADO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Estado no contemplado para movimiento: 4");
    }

    @Test
    void registrar_conDetalleNulo_creaMovimientoConDetalleNuloParaFormulario() {
        Movimiento resultado = movimientoService.registrar(null, empleado(), null, OrigenMovimiento.FORMULARIO_EDICION);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(TipoMovimiento.EDITAR_ORDEN);
        assertThat(resultado.getDetalle()).isNull();
    }

    @Test
    void registrar_conEmpleadoNulo_creaMovimientoConEmpleadoNulo() {
        Movimiento resultado = movimientoService.registrar(null, null, "detalle", OrigenMovimiento.FORMULARIO_CREACION);

        assertThat(resultado.getTipoMovimiento()).isEqualTo(TipoMovimiento.TOMAR_PEDIDO);
        assertThat(resultado.getEmpleado()).isNull();
    }

    @Test
    void buscarPorOrden_conOrdenConMovimientos_delegaEnRepositorio() {
        List<Movimiento> movimientos = List.of(new Movimiento(), new Movimiento());
        when(movimientoRepository.findByOrdenIdOrderByFechaDesc(10L)).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.buscarPorOrden(10L);

        assertThat(resultado).isSameAs(movimientos);
    }

    @Test
    void buscarPorOrden_conOrdenSinMovimientos_devuelveListaVacia() {
        when(movimientoRepository.findByOrdenIdOrderByFechaDesc(10L)).thenReturn(List.of());

        List<Movimiento> resultado = movimientoService.buscarPorOrden(10L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void buscarPorOrden_conIdOrdenNulo_delegaEnRepositorio() {
        when(movimientoRepository.findByOrdenIdOrderByFechaDesc(null)).thenReturn(List.of());

        List<Movimiento> resultado = movimientoService.buscarPorOrden(null);

        assertThat(resultado).isEmpty();
    }

    private Empleado empleado() {
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setUsername("usuario");
        return empleado;
    }

    private static Stream<Arguments> movimientosPorCambioDeEstado() {
        return Stream.of(
                Arguments.of("sin hacer", 1L, TipoMovimiento.VOLVER_A_SIN_HACER),
                Arguments.of("en proceso", 2L, TipoMovimiento.PASAR_A_EN_PROCESO),
                Arguments.of("lista para retirar", 3L, TipoMovimiento.MARCAR_LISTA_PARA_RETIRAR),
                Arguments.of("retirado", 5L, TipoMovimiento.MARCAR_PEDIDO_COMO_RETIRADO)
        );
    }

    private static Stream<Arguments> movimientosPorOrigenFormulario() {
        return Stream.of(
                Arguments.of("creación", OrigenMovimiento.FORMULARIO_CREACION, TipoMovimiento.TOMAR_PEDIDO),
                Arguments.of("edición", OrigenMovimiento.FORMULARIO_EDICION, TipoMovimiento.EDITAR_ORDEN),
                Arguments.of("corrección", OrigenMovimiento.FORMULARIO_CORRECCION, TipoMovimiento.CORREGIR_ORDEN)
        );
    }
}
