package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.EstadoPago;
import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.OrigenMovimiento;
import com.puntografico.puntografico.domain.Pago;
import com.puntografico.puntografico.domain.Rol;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.MovimientoService;
import com.puntografico.puntografico.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PagoService.class)
class PagoServiceTest {

    private static final long ID_ESTADO_SIN_PAGAR = 1L;
    private static final long ID_ESTADO_SENIADO = 2L;
    private static final long ID_ESTADO_PAGADO = 3L;
    private static final long ID_ORDEN = 10L;
    private static final long ID_MEDIO_PAGO = 20L;

    @Autowired
    private PagoService pagoService;

    @MockBean
    private EstadoPagoRepository estadoPagoRepository;

    @MockBean
    private OrdenRepository ordenRepository;

    @MockBean
    private PagoRepository pagoRepository;

    @MockBean
    private MedioPagoService medioPagoService;

    @MockBean
    private MovimientoService movimientoService;

    private Empleado empleadoOrden;
    private Empleado empleadoLogueado;
    private MedioPago medioPago;
    private EstadoPago estadoSinPagar;
    private EstadoPago estadoSeniado;
    private EstadoPago estadoPagado;

    @BeforeEach
    void setUp() {
        empleadoOrden = empleado(1L, "vendedor");
        empleadoLogueado = empleado(2L, "cajero");
        medioPago = medioPago(ID_MEDIO_PAGO, "Efectivo");
        estadoSinPagar = estadoPago(ID_ESTADO_SIN_PAGAR, "Sin pagar");
        estadoSeniado = estadoPago(ID_ESTADO_SENIADO, "Señado");
        estadoPagado = estadoPago(ID_ESTADO_PAGADO, "Pagado");

        when(movimientoService.registrar(any(), any(), any(), any()))
                .thenAnswer(invocation -> new Movimiento());
    }

    @Test
    void registrarPagoExtra_conPagoParcial_guardaPagoActualizaAbonadoEstadoMovimientoYOrden() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 200)));
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));
        when(medioPagoService.buscarPorId(ID_MEDIO_PAGO)).thenReturn(medioPago);
        when(estadoPagoRepository.findById(ID_ESTADO_SENIADO)).thenReturn(Optional.of(estadoSeniado));

        pagoService.registrarPagoExtra(ID_ORDEN, 300, ID_MEDIO_PAGO, empleadoLogueado);

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(pagoCaptor.capture());
        Pago pagoGuardado = pagoCaptor.getValue();

        assertThat(pagoGuardado.getImporte()).isEqualTo(300);
        assertThat(pagoGuardado.getMedioPago()).isSameAs(medioPago);
        assertThat(pagoGuardado.getFechaPago()).isEqualTo(LocalDate.now());
        assertThat(pagoGuardado.getOrden()).isSameAs(orden);
        assertThat(pagoGuardado.getEmpleado()).isSameAs(empleadoOrden);
        assertThat(orden.getPagos()).hasSize(1);
        assertThat(orden.getAbonado()).isEqualTo(200);
        assertThat(orden.getEstadoPago()).isSameAs(estadoSeniado);
        assertThat(orden.getMovimientos()).hasSize(1);
        assertThat(orden.getMovimientos().get(0).getOrden()).isSameAs(orden);

        verify(pagoRepository).flush();
        verify(movimientoService).registrar(isNull(), eq(empleadoLogueado), eq("300"), eq(OrigenMovimiento.REGISTRO_PAGO));
        verify(ordenRepository).save(orden);
    }

    @Test
    void registrarPagoExtra_conPagoQueCompletariaTotal_mantieneEstadoSegunPagosEnMemoria() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 700)));
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));
        when(medioPagoService.buscarPorId(ID_MEDIO_PAGO)).thenReturn(medioPago);
        when(estadoPagoRepository.findById(ID_ESTADO_PAGADO)).thenReturn(Optional.of(estadoPagado));
        when(estadoPagoRepository.findById(ID_ESTADO_SENIADO)).thenReturn(Optional.of(estadoSeniado));

        pagoService.registrarPagoExtra(ID_ORDEN, 300, ID_MEDIO_PAGO, empleadoLogueado);

        assertThat(orden.getAbonado()).isEqualTo(700);
        assertThat(orden.getEstadoPago()).isSameAs(estadoSeniado);
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void registrarPagoExtra_conOrdenSinPagosPrevios_guardaPagoPeroMantieneAbonadoEnCero() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));
        when(medioPagoService.buscarPorId(ID_MEDIO_PAGO)).thenReturn(medioPago);
        when(estadoPagoRepository.findById(ID_ESTADO_SIN_PAGAR)).thenReturn(Optional.of(estadoSinPagar));

        pagoService.registrarPagoExtra(ID_ORDEN, 250, ID_MEDIO_PAGO, empleadoLogueado);

        assertThat(orden.getAbonado()).isZero();
        assertThat(orden.getPagos()).isEmpty();
        assertThat(orden.getEstadoPago()).isSameAs(estadoSinPagar);
        verify(pagoRepository).save(any(Pago.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("datosInvalidosParaPagoExtra")
    void registrarPagoExtra_conDatosInvalidos_lanzaExcepcionActualDelServicio(
            String caso,
            Long idOrden,
            Integer montoAbonado,
            Long idMedioPago,
            Empleado empleado,
            Class<? extends Throwable> tipoEsperado
    ) {
        assertThatThrownBy(() -> pagoService.registrarPagoExtra(idOrden, montoAbonado, idMedioPago, empleado))
                .isInstanceOf(tipoEsperado);
    }

    @Test
    void registrarPagoExtra_conOrdenInexistente_lanzaExcepcion() {
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.registrarPagoExtra(ID_ORDEN, 300, ID_MEDIO_PAGO, empleadoLogueado))
                .isInstanceOf(NoSuchElementException.class);

        verifyNoInteractions(pagoRepository, medioPagoService, movimientoService, estadoPagoRepository);
        verify(ordenRepository, never()).save(any());
    }

    @Test
    void crearPagoDesdeFormularioOrden_conAbonadoMayorACero_agregaPagoALaOrden() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        orden.setAbonado(400);
        orden.setFechaPedido(LocalDate.of(2026, 6, 2));
        when(medioPagoService.buscarPorId(ID_MEDIO_PAGO)).thenReturn(medioPago);

        pagoService.crearPagoDesdeFormularioOrden(orden, ID_MEDIO_PAGO);

        assertThat(orden.getPagos()).hasSize(1);
        Pago pagoCreado = orden.getPagos().get(0);
        assertThat(pagoCreado.getImporte()).isEqualTo(400);
        assertThat(pagoCreado.getMedioPago()).isSameAs(medioPago);
        assertThat(pagoCreado.getFechaPago()).isEqualTo(LocalDate.of(2026, 6, 2));
        assertThat(pagoCreado.getOrden()).isSameAs(orden);
        assertThat(pagoCreado.getEmpleado()).isSameAs(empleadoOrden);
    }

    @Test
    void crearPagoDesdeFormularioOrden_conAbonadoCero_noAgregaPago() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        orden.setAbonado(0);

        pagoService.crearPagoDesdeFormularioOrden(orden, ID_MEDIO_PAGO);

        assertThat(orden.getPagos()).isEmpty();
        verifyNoInteractions(medioPagoService);
    }

    @Test
    void crearPagoDesdeFormularioOrden_conAbonadoNegativo_noAgregaPago() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        orden.setAbonado(-100);

        pagoService.crearPagoDesdeFormularioOrden(orden, ID_MEDIO_PAGO);

        assertThat(orden.getPagos()).isEmpty();
        verifyNoInteractions(medioPagoService);
    }

    @Test
    void crearPagoDesdeFormularioOrden_conOrdenNula_lanzaExcepcion() {
        assertThatThrownBy(() -> pagoService.crearPagoDesdeFormularioOrden(null, ID_MEDIO_PAGO))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(medioPagoService);
    }

    @Test
    void crearPagoDesdeFormularioOrden_conAbonadoMayorACeroYMedioPagoNulo_agregaPagoConMedioPagoNulo() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        orden.setAbonado(400);

        pagoService.crearPagoDesdeFormularioOrden(orden, null);

        assertThat(orden.getPagos()).hasSize(1);
        assertThat(orden.getPagos().get(0).getMedioPago()).isNull();
        verify(medioPagoService).buscarPorId(null);
    }

    @Test
    void actualizarEstadoPago_conOrdenSinPagos_asignaEstadoSinPagar() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        when(estadoPagoRepository.findById(ID_ESTADO_SIN_PAGAR)).thenReturn(Optional.of(estadoSinPagar));

        pagoService.actualizarEstadoPago(orden);

        assertThat(orden.getEstadoPago()).isSameAs(estadoSinPagar);
    }

    @Test
    void actualizarEstadoPago_conPagosParciales_asignaEstadoSeniado() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 300), pago(2L, 200)));
        when(estadoPagoRepository.findById(ID_ESTADO_SENIADO)).thenReturn(Optional.of(estadoSeniado));

        pagoService.actualizarEstadoPago(orden);

        assertThat(orden.getEstadoPago()).isSameAs(estadoSeniado);
    }

    @Test
    void actualizarEstadoPago_conPagosIgualesAlTotal_asignaEstadoPagado() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 600), pago(2L, 400)));
        when(estadoPagoRepository.findById(ID_ESTADO_PAGADO)).thenReturn(Optional.of(estadoPagado));

        pagoService.actualizarEstadoPago(orden);

        assertThat(orden.getEstadoPago()).isSameAs(estadoPagado);
    }

    @Test
    void actualizarEstadoPago_conPagosMayoresAlTotal_asignaEstadoSeniado() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 1200)));
        when(estadoPagoRepository.findById(ID_ESTADO_SENIADO)).thenReturn(Optional.of(estadoSeniado));

        pagoService.actualizarEstadoPago(orden);

        assertThat(orden.getEstadoPago()).isSameAs(estadoSeniado);
    }

    @Test
    void actualizarEstadoPago_conOrdenNula_lanzaExcepcion() {
        assertThatThrownBy(() -> pagoService.actualizarEstadoPago(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(estadoPagoRepository);
    }

    @Test
    void actualizarEstadoPago_conEstadoNoEncontrado_lanzaExcepcion() {
        Orden orden = orden(ID_ORDEN, 1000, List.of());
        when(estadoPagoRepository.findById(ID_ESTADO_SIN_PAGAR)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.actualizarEstadoPago(orden))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void eliminarPagosAsociados_conPagosExistentes_eliminaPagosYLimpiaOrden() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 200), pago(2L, 300)));
        Pago primerPago = pago(1L, 200);
        Pago segundoPago = pago(2L, 300);
        when(pagoRepository.findByOrdenId(ID_ORDEN)).thenReturn(List.of(primerPago, segundoPago));
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));

        pagoService.eliminarPagosAsociados(ID_ORDEN);

        verify(pagoRepository).deleteById(1L);
        verify(pagoRepository).deleteById(2L);
        assertThat(orden.getPagos()).isEmpty();
    }

    @Test
    void eliminarPagosAsociados_conOrdenSinPagosAsociados_soloLimpiaOrden() {
        Orden orden = orden(ID_ORDEN, 1000, List.of(pago(1L, 200)));
        when(pagoRepository.findByOrdenId(ID_ORDEN)).thenReturn(List.of());
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));

        pagoService.eliminarPagosAsociados(ID_ORDEN);

        verify(pagoRepository, never()).deleteById(any());
        assertThat(orden.getPagos()).isEmpty();
    }

    @Test
    void eliminarPagosAsociados_conIdOrdenNulo_lanzaExcepcion() {
        assertThatThrownBy(() -> pagoService.eliminarPagosAsociados(null))
                .isInstanceOf(NoSuchElementException.class);

        verify(pagoRepository).findByOrdenId(null);
        verify(ordenRepository).findById(null);
    }

    @Test
    void eliminarPagosAsociados_conOrdenInexistente_lanzaExcepcion() {
        when(pagoRepository.findByOrdenId(ID_ORDEN)).thenReturn(List.of());
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.eliminarPagosAsociados(ID_ORDEN))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Orden orden(Long id, int total, List<Pago> pagos) {
        Orden orden = new Orden();
        orden.setId(id);
        orden.setNombreCliente("Cliente");
        orden.setTelefonoCliente("123456");
        orden.setFechaPedido(LocalDate.of(2026, 6, 1));
        orden.setFechaEntrega(LocalDate.of(2026, 6, 3));
        orden.setHoraEntrega("10:00");
        orden.setTotal(total);
        orden.setSubtotal(total);
        orden.setAbonado(0);
        orden.setEmpleado(empleadoOrden);
        orden.setPagos(new ArrayList<>());
        orden.setMovimientos(new ArrayList<>());

        pagos.forEach(pago -> {
            pago.setOrden(orden);
            pago.setEmpleado(empleadoOrden);
            orden.getPagos().add(pago);
        });

        return orden;
    }

    private Pago pago(Long id, int importe) {
        Pago pago = new Pago();
        pago.setId(id);
        pago.setImporte(importe);
        pago.setFechaPago(LocalDate.of(2026, 6, 1));
        pago.setMedioPago(medioPago);
        return pago;
    }

    private EstadoPago estadoPago(Long id, String nombre) {
        EstadoPago estadoPago = new EstadoPago();
        estadoPago.setId(id);
        estadoPago.setEstadoDePago(nombre);
        return estadoPago;
    }

    private MedioPago medioPago(Long id, String nombre) {
        MedioPago medioPago = new MedioPago();
        medioPago.setId(id);
        medioPago.setMedioDePago(nombre);
        return medioPago;
    }

    private Empleado empleado(Long id, String username) {
        Rol rol = new Rol();
        rol.setId(3L);
        rol.setNombre("Rol");

        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setUsername(username);
        empleado.setNombre("Empleado " + id);
        empleado.setRol(rol);
        return empleado;
    }

    private static Stream<Arguments> datosInvalidosParaPagoExtra() {
        Empleado empleado = new Empleado();

        return Stream.of(
                Arguments.of(
                        "idOrden null",
                        null,
                        300,
                        ID_MEDIO_PAGO,
                        empleado,
                        NoSuchElementException.class
                ),
                Arguments.of(
                        "montoAbonado null",
                        ID_ORDEN,
                        null,
                        ID_MEDIO_PAGO,
                        empleado,
                        NoSuchElementException.class
                ),
                Arguments.of(
                        "montoAbonado cero",
                        ID_ORDEN,
                        0,
                        ID_MEDIO_PAGO,
                        empleado,
                        NoSuchElementException.class
                ),
                Arguments.of(
                        "montoAbonado negativo",
                        ID_ORDEN,
                        -100,
                        ID_MEDIO_PAGO,
                        empleado,
                        NoSuchElementException.class
                ),
                Arguments.of(
                        "idMedioPago null",
                        ID_ORDEN,
                        300,
                        null,
                        empleado,
                        NoSuchElementException.class
                ),
                Arguments.of(
                        "empleado null",
                        ID_ORDEN,
                        300,
                        ID_MEDIO_PAGO,
                        null,
                        NoSuchElementException.class
                )
        );
    }
}
