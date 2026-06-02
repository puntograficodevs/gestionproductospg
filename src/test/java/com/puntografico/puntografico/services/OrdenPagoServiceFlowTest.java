package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.EstadoOrden;
import com.puntografico.puntografico.domain.EstadoPago;
import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.OrigenMovimiento;
import com.puntografico.puntografico.domain.Pago;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.domain.Rol;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.MovimientoService;
import com.puntografico.puntografico.service.OrdenService;
import com.puntografico.puntografico.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {OrdenService.class, PagoService.class})
class OrdenPagoServiceFlowTest {

    private static final long ID_ORDEN = 10L;
    private static final int ID_PRODUCTO = 20;
    private static final long ID_MEDIO_PAGO = 30L;
    private static final long ID_ESTADO_SENIADO = 2L;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private PagoService pagoService;

    @MockBean
    private OrdenRepository ordenRepository;

    @MockBean
    private EstadoOrdenRepository estadoOrdenRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private EstadoPagoRepository estadoPagoRepository;

    @MockBean
    private PagoRepository pagoRepository;

    @MockBean
    private MedioPagoService medioPagoService;

    @MockBean
    private MovimientoService movimientoService;

    private Empleado empleado;
    private MedioPago medioPago;
    private EstadoPago estadoSeniado;

    @BeforeEach
    void setUp() {
        empleado = empleado();
        medioPago = medioPago();
        estadoSeniado = estadoPago(ID_ESTADO_SENIADO, "Señado");

        when(movimientoService.registrar(any(), any(), any(), any()))
                .thenAnswer(invocation -> new Movimiento());
    }

    @Test
    void guardarYRegistrarPagoExtra_conCorreccionDeAbonadoYNuevoPago_reemplazaPagoAnteriorYCalculaRestanteCorrecto() {
        Pago pagoOriginal = pago(1L, 5000);
        Orden ordenPersistida = orden(ID_ORDEN, 8000, 5000, List.of(pagoOriginal));
        Orden ordenModificada = orden(ID_ORDEN, 8000, 6000, List.of());

        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(ordenPersistida));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto()));
        when(pagoRepository.findByOrdenId(ID_ORDEN)).thenReturn(List.of(pagoOriginal));
        when(medioPagoService.buscarPorId(ID_MEDIO_PAGO)).thenReturn(medioPago);
        when(estadoPagoRepository.findById(ID_ESTADO_SENIADO)).thenReturn(Optional.of(estadoSeniado));
        when(ordenRepository.save(ordenPersistida)).thenReturn(ordenPersistida);

        Orden resultado = ordenService.guardar(ordenModificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado.getTotal()).isEqualTo(8000);
        assertThat(resultado.getAbonado()).isEqualTo(6000);
        assertThat(resultado.getTotal() - resultado.getAbonado()).isEqualTo(2000);
        assertThat(resultado.getPagos())
                .extracting(Pago::getImporte)
                .containsExactly(6000);
        verify(pagoRepository).deleteById(1L);

        pagoService.registrarPagoExtra(ID_ORDEN, 1000, ID_MEDIO_PAGO, empleado);

        assertThat(resultado.getAbonado()).isEqualTo(6000);
        assertThat(resultado.getTotal() - resultado.getAbonado()).isEqualTo(2000);
        assertThat(resultado.getPagos())
                .extracting(Pago::getImporte)
                .containsExactly(6000);
        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(pagoCaptor.capture());
        assertThat(pagoCaptor.getValue().getImporte()).isEqualTo(1000);
        assertThat(pagoCaptor.getValue().getOrden()).isSameAs(resultado);
        verify(movimientoService).registrar(isNull(), eq(empleado), eq("Se modifica abonado a: $6000"), eq(OrigenMovimiento.FORMULARIO_EDICION));
        verify(movimientoService).registrar(isNull(), eq(empleado), eq("1000"), eq(OrigenMovimiento.REGISTRO_PAGO));
    }

    private Orden orden(Long id, int total, int abonado, List<Pago> pagos) {
        Orden orden = new Orden();
        orden.setId(id);
        orden.setNombreCliente("Cliente");
        orden.setTelefonoCliente("123456");
        orden.setEsCuentaCorriente(false);
        orden.setFechaPedido(LocalDate.of(2026, 6, 1));
        orden.setFechaEntrega(LocalDate.of(2026, 6, 5));
        orden.setHoraEntrega("10:00");
        orden.setNecesitaFactura(false);
        orden.setTotal(total);
        orden.setSubtotal(total);
        orden.setPrecioDisenio(0);
        orden.setAbonado(abonado);
        orden.setEstadoOrden(estadoOrden());
        orden.setEmpleado(empleado);
        orden.setItems(new ArrayList<>());
        orden.setPagos(new ArrayList<>());
        orden.setMovimientos(new ArrayList<>());

        pagos.forEach(pago -> {
            pago.setOrden(orden);
            pago.setEmpleado(empleado);
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

    private Producto producto() {
        Producto producto = new Producto();
        producto.setId(ID_PRODUCTO);
        producto.setNombre("Producto");
        return producto;
    }

    private EstadoOrden estadoOrden() {
        EstadoOrden estadoOrden = new EstadoOrden();
        estadoOrden.setId(1L);
        estadoOrden.setEstadoDeOrden("Sin hacer");
        return estadoOrden;
    }

    private EstadoPago estadoPago(Long id, String nombre) {
        EstadoPago estadoPago = new EstadoPago();
        estadoPago.setId(id);
        estadoPago.setEstadoDePago(nombre);
        return estadoPago;
    }

    private MedioPago medioPago() {
        MedioPago medioPago = new MedioPago();
        medioPago.setId(ID_MEDIO_PAGO);
        medioPago.setMedioDePago("Efectivo");
        return medioPago;
    }

    private Empleado empleado() {
        Rol rol = new Rol();
        rol.setId(3L);
        rol.setNombre("Rol");

        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setUsername("usuario");
        empleado.setNombre("Empleado");
        empleado.setRol(rol);
        return empleado;
    }
}
