package com.puntografico.puntografico.integration;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.EstadoOrden;
import com.puntografico.puntografico.domain.EstadoPago;
import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.OrdenItem;
import com.puntografico.puntografico.domain.Pago;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.domain.Rol;
import com.puntografico.puntografico.domain.TipoMovimiento;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.EstadoPagoRepository;
import com.puntografico.puntografico.repository.MedioPagoRepository;
import com.puntografico.puntografico.repository.MovimientoRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.PagoRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.MovimientoService;
import com.puntografico.puntografico.service.OrdenService;
import com.puntografico.puntografico.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {OrdenService.class, PagoService.class, MedioPagoService.class, MovimientoService.class})
class OrdenPagoMovimientoIntegrationTest {

    private static final long ID_ORDEN = 10L;
    private static final int ID_PRODUCTO = 20;
    private static final long ID_MEDIO_PAGO = 30L;
    private static final long ID_ESTADO_SIN_HACER = 1L;
    private static final long ID_ESTADO_EN_PROCESO = 2L;
    private static final long ID_ESTADO_CORRECCION = 4L;
    private static final long ID_ESTADO_SIN_PAGAR = 1L;
    private static final long ID_ESTADO_SENIADO = 2L;
    private static final long ID_ESTADO_PAGADO = 3L;

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
    private MedioPagoRepository medioPagoRepository;

    @MockBean
    private MovimientoRepository movimientoRepository;

    private Empleado empleado;
    private Producto producto;
    private MedioPago medioPago;
    private EstadoOrden estadoSinHacer;
    private EstadoOrden estadoEnProceso;
    private EstadoOrden estadoCorreccion;
    private EstadoPago estadoSinPagar;
    private EstadoPago estadoSeniado;
    private EstadoPago estadoPagado;

    @BeforeEach
    void setUp() {
        empleado = empleado();
        producto = producto();
        medioPago = medioPago();
        estadoSinHacer = estadoOrden(ID_ESTADO_SIN_HACER, "Sin hacer");
        estadoEnProceso = estadoOrden(ID_ESTADO_EN_PROCESO, "En proceso");
        estadoCorreccion = estadoOrden(ID_ESTADO_CORRECCION, "Correccion");
        estadoSinPagar = estadoPago(ID_ESTADO_SIN_PAGAR, "Sin pagar");
        estadoSeniado = estadoPago(ID_ESTADO_SENIADO, "Señado");
        estadoPagado = estadoPago(ID_ESTADO_PAGADO, "Pagado");

        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(medioPagoRepository.findById(ID_MEDIO_PAGO)).thenReturn(Optional.of(medioPago));
        when(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER)).thenReturn(Optional.of(estadoSinHacer));
        when(estadoOrdenRepository.findById(ID_ESTADO_EN_PROCESO)).thenReturn(Optional.of(estadoEnProceso));
        when(estadoOrdenRepository.findById(ID_ESTADO_CORRECCION)).thenReturn(Optional.of(estadoCorreccion));
        when(estadoPagoRepository.findById(ID_ESTADO_SIN_PAGAR)).thenReturn(Optional.of(estadoSinPagar));
        when(estadoPagoRepository.findById(ID_ESTADO_SENIADO)).thenReturn(Optional.of(estadoSeniado));
        when(estadoPagoRepository.findById(ID_ESTADO_PAGADO)).thenReturn(Optional.of(estadoPagado));
        when(ordenRepository.save(any(Orden.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void guardar_conOrdenNuevaPagadaYFactura_creaPagoMovimientoYNoModificaPrecioPorFactura() {
        Orden orden = orden(null, 8000, 8000, true, estadoSinHacer);
        OrdenItem item = item();
        orden.getItems().add(item);

        Orden resultado = ordenService.guardar(orden, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado.getTotal()).isEqualTo(8000);
        assertThat(resultado.getSubtotal()).isEqualTo(8000);
        assertThat(resultado.isNecesitaFactura()).isTrue();
        assertThat(resultado.getPagos()).hasSize(1);
        assertThat(resultado.getPagos().get(0).getImporte()).isEqualTo(8000);
        assertThat(resultado.getPagos().get(0).getMedioPago()).isSameAs(medioPago);
        assertThat(resultado.getEstadoPago()).isSameAs(estadoPagado);
        assertThat(item.getOrden()).isSameAs(resultado);
        assertThat(item.getProducto()).isSameAs(producto);
        assertThat(resultado.getMovimientos()).hasSize(1);
        assertThat(resultado.getMovimientos().get(0).getTipoMovimiento()).isEqualTo(TipoMovimiento.TOMAR_PEDIDO);
        assertThat(resultado.getMovimientos().get(0).getOrden()).isSameAs(resultado);

        verify(ordenRepository).save(orden);
    }

    @Test
    void guardar_conOrdenNuevaSinAbonado_creaOrdenSinPagosYEstadoSinPagar() {
        Orden orden = orden(null, 8000, 0, false, estadoSinHacer);

        Orden resultado = ordenService.guardar(orden, ID_PRODUCTO, null, empleado);

        assertThat(resultado.getPagos()).isEmpty();
        assertThat(resultado.getEstadoPago()).isSameAs(estadoSinPagar);
        assertThat(resultado.getMovimientos())
                .extracting(Movimiento::getTipoMovimiento)
                .containsExactly(TipoMovimiento.TOMAR_PEDIDO);
    }

    @Test
    void guardar_conEdicionQueSoloActivaFactura_noModificaPrecioNiRecreaPagos() {
        Pago pagoOriginal = pago(1L, 5000);
        Orden ordenPersistida = orden(ID_ORDEN, 8000, 5000, false, estadoSinHacer);
        ordenPersistida.getPagos().add(pagoOriginal);
        pagoOriginal.setOrden(ordenPersistida);

        Orden ordenModificada = orden(ID_ORDEN, 8000, 5000, true, estadoSinHacer);
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(ordenPersistida));

        Orden resultado = ordenService.guardar(ordenModificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado.getTotal()).isEqualTo(8000);
        assertThat(resultado.getAbonado()).isEqualTo(5000);
        assertThat(resultado.isNecesitaFactura()).isTrue();
        assertThat(resultado.getPagos()).containsExactly(pagoOriginal);
        assertThat(resultado.getEstadoPago()).isSameAs(estadoSeniado);
        assertThat(resultado.getMovimientos())
                .extracting(Movimiento::getTipoMovimiento)
                .containsExactly(TipoMovimiento.EDITAR_ORDEN);

        verify(pagoRepository, never()).deleteById(any());
    }

    @Test
    void guardar_conCorreccionResuelta_restauraEstadoAnteriorLimpiaCorreccionYRegistraMovimiento() {
        Orden ordenPersistida = orden(ID_ORDEN, 8000, 5000, false, estadoCorreccion);
        ordenPersistida.setCorreccion("Cambiar medidas");
        ordenPersistida.setIdEstadoPrevio((int) ID_ESTADO_EN_PROCESO);
        ordenPersistida.getPagos().add(pago(1L, 5000));

        Orden ordenModificada = orden(ID_ORDEN, 8000, 5000, false, estadoCorreccion);
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(ordenPersistida));

        Orden resultado = ordenService.guardar(ordenModificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado.getEstadoOrden()).isSameAs(estadoEnProceso);
        assertThat(resultado.getCorreccion()).isNull();
        assertThat(resultado.getIdEstadoPrevio()).isNull();
        assertThat(resultado.getMovimientos()).hasSize(1);
        assertThat(resultado.getMovimientos().get(0).getTipoMovimiento()).isEqualTo(TipoMovimiento.CORREGIR_ORDEN);
        assertThat(resultado.getMovimientos().get(0).getDetalle())
                .isEqualTo("El pedido de corrección era: Cambiar medidas.");
    }

    @Test
    void cambiarEstadoOrden_conAsignarEncargado_asignaEmpleadoCambiaEstadoYRegistraMovimiento() {
        Orden orden = orden(ID_ORDEN, 8000, 0, false, estadoSinHacer);
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));

        ordenService.cambiarEstadoOrden(ID_ORDEN, ID_ESTADO_EN_PROCESO, true, empleado);

        assertThat(orden.getEstadoOrden()).isSameAs(estadoEnProceso);
        assertThat(orden.getEncargadoProduccion()).isSameAs(empleado);
        assertThat(orden.getMovimientos()).hasSize(1);
        assertThat(orden.getMovimientos().get(0).getTipoMovimiento()).isEqualTo(TipoMovimiento.PASAR_A_EN_PROCESO);
        assertThat(orden.getMovimientos().get(0).getEmpleado()).isSameAs(empleado);
        verify(ordenRepository).save(orden);
    }

    @Test
    void enviarAColumnaCorreccion_conMotivo_guardaEstadoPrevioMotivoMovimientoYOrden() {
        Orden orden = orden(ID_ORDEN, 8000, 0, false, estadoEnProceso);
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));

        ordenService.enviarAColumnaCorreccion(ID_ORDEN, "Revisar archivo", empleado);

        assertThat(orden.getIdEstadoPrevio()).isEqualTo((int) ID_ESTADO_EN_PROCESO);
        assertThat(orden.getEstadoOrden()).isSameAs(estadoCorreccion);
        assertThat(orden.getCorreccion()).isEqualTo("Revisar archivo");
        assertThat(orden.getMovimientos()).hasSize(1);
        assertThat(orden.getMovimientos().get(0).getTipoMovimiento()).isEqualTo(TipoMovimiento.PEDIR_CORRECCION);
        assertThat(orden.getMovimientos().get(0).getDetalle()).isEqualTo("Se pidió una corrección: Revisar archivo");
        verify(ordenRepository).save(orden);
    }

    @Test
    void registrarPagoExtra_conOrdenSeniada_persistePagoActualizaRestanteYMovimiento() {
        Pago pagoOriginal = pago(1L, 5000);
        Orden orden = orden(ID_ORDEN, 8000, 5000, false, estadoSinHacer);
        orden.getPagos().add(pagoOriginal);
        pagoOriginal.setOrden(orden);
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));

        pagoService.registrarPagoExtra(ID_ORDEN, 1000, ID_MEDIO_PAGO, empleado);

        assertThat(orden.getPagos()).hasSize(2);
        assertThat(orden.getPagos())
                .extracting(Pago::getImporte)
                .containsExactly(5000, 1000);
        assertThat(orden.getAbonado()).isEqualTo(6000);
        assertThat(orden.getTotal() - orden.getAbonado()).isEqualTo(2000);
        assertThat(orden.getEstadoPago()).isSameAs(estadoSeniado);
        assertThat(orden.getMovimientos()).hasSize(1);
        assertThat(orden.getMovimientos().get(0).getTipoMovimiento()).isEqualTo(TipoMovimiento.REGISTRAR_PAGO);
        assertThat(orden.getMovimientos().get(0).getDetalle()).isEqualTo("Se registró pago de $1000");

        verify(pagoRepository).save(orden.getPagos().get(1));
        verify(ordenRepository).save(orden);
    }

    private Orden orden(Long id, int total, int abonado, boolean necesitaFactura, EstadoOrden estadoOrden) {
        Orden orden = new Orden();
        orden.setId(id);
        orden.setNombreCliente("Cliente");
        orden.setTelefonoCliente("123");
        orden.setFechaPedido(LocalDate.of(2026, 6, 1));
        orden.setFechaEntrega(LocalDate.of(2026, 6, 5));
        orden.setHoraEntrega("10:00");
        orden.setTotal(total);
        orden.setSubtotal(total);
        orden.setPrecioDisenio(0);
        orden.setAbonado(abonado);
        orden.setNecesitaFactura(necesitaFactura);
        orden.setEstadoOrden(estadoOrden);
        orden.setEmpleado(empleado);
        orden.setItems(new ArrayList<>());
        orden.setPagos(new ArrayList<>());
        orden.setMovimientos(new ArrayList<>());
        return orden;
    }

    private OrdenItem item() {
        OrdenItem item = new OrdenItem();
        item.setCantidad(1);
        item.setPrecioUnitario(8000);
        return item;
    }

    private Pago pago(Long id, int importe) {
        Pago pago = new Pago();
        pago.setId(id);
        pago.setImporte(importe);
        pago.setFechaPago(LocalDate.of(2026, 6, 1));
        pago.setMedioPago(medioPago);
        pago.setEmpleado(empleado);
        return pago;
    }

    private Producto producto() {
        Producto producto = new Producto();
        producto.setId(ID_PRODUCTO);
        producto.setNombre("Producto");
        return producto;
    }

    private MedioPago medioPago() {
        MedioPago medioPago = new MedioPago();
        medioPago.setId(ID_MEDIO_PAGO);
        medioPago.setMedioDePago("Efectivo");
        return medioPago;
    }

    private EstadoOrden estadoOrden(Long id, String nombre) {
        EstadoOrden estadoOrden = new EstadoOrden();
        estadoOrden.setId(id);
        estadoOrden.setEstadoDeOrden(nombre);
        return estadoOrden;
    }

    private EstadoPago estadoPago(Long id, String nombre) {
        EstadoPago estadoPago = new EstadoPago();
        estadoPago.setId(id);
        estadoPago.setEstadoDePago(nombre);
        return estadoPago;
    }

    private Empleado empleado() {
        Rol rol = new Rol();
        rol.setId(3L);
        rol.setNombre("Administracion");

        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setUsername("usuario");
        empleado.setNombre("Empleado");
        empleado.setRol(rol);
        return empleado;
    }
}
