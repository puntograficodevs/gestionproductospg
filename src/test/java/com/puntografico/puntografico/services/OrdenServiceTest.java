package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.EstadoOrden;
import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.OrdenItem;
import com.puntografico.puntografico.domain.OrigenMovimiento;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.domain.Rol;
import com.puntografico.puntografico.repository.EstadoOrdenRepository;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.repository.ProductoRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrdenService.class)
class OrdenServiceTest {

    private static final long ID_ESTADO_SIN_HACER = 1L;
    private static final long ID_ESTADO_EN_PROCESO = 2L;
    private static final long ID_ESTADO_CORRECCION = 4L;
    private static final int ID_PRODUCTO = 10;
    private static final long ID_MEDIO_PAGO = 20L;

    @Autowired
    private OrdenService ordenService;

    @MockBean
    private OrdenRepository ordenRepository;

    @MockBean
    private EstadoOrdenRepository estadoOrdenRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private MovimientoService movimientoService;

    private Empleado empleado;
    private Producto producto;
    private EstadoOrden estadoSinHacer;
    private EstadoOrden estadoEnProceso;
    private EstadoOrden estadoCorreccion;

    @BeforeEach
    void setUp() {
        empleado = empleado(1L, "usuario", 3L);
        producto = producto(ID_PRODUCTO);
        estadoSinHacer = estadoOrden(ID_ESTADO_SIN_HACER);
        estadoEnProceso = estadoOrden(ID_ESTADO_EN_PROCESO);
        estadoCorreccion = estadoOrden(ID_ESTADO_CORRECCION);

        when(movimientoService.registrar(any(), any(), any(), any()))
                .thenAnswer(invocation -> new Movimiento());
    }

    @Test
    void buscarPorIdDevuelveOrdenCuandoExiste() {
        Orden orden = orden(5L, "Cliente", 1000, 0, estadoSinHacer);
        when(ordenRepository.findById(5L)).thenReturn(Optional.of(orden));

        Orden resultado = ordenService.buscarPorId(5L);

        assertThat(resultado).isSameAs(orden);
    }

    @Test
    void buscarPorIdFallaCuandoIdEsNulo() {
        assertThatThrownBy(() -> ordenService.buscarPorId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El ID de la orden no puede venir nulo.");
    }

    @Test
    void buscarPorIdFallaCuandoNoExiste() {
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Orden no encontrada con id: 99");
    }

    @Test
    void eliminarBorraCuandoExiste() {
        when(ordenRepository.existsById(7L)).thenReturn(true);

        ordenService.eliminar(7L);

        verify(ordenRepository).deleteById(7L);
    }

    @Test
    void eliminarNoBorraCuandoNoExiste() {
        when(ordenRepository.existsById(7L)).thenReturn(false);

        ordenService.eliminar(7L);

        verify(ordenRepository, never()).deleteById(anyLong());
    }

    @Test
    void guardarCreaOrdenNuevaConEstadoPagoItemsMovimientoYFechaPedido() {
        Orden ordenNueva = orden(null, "Cliente", 1000, 300, null);
        OrdenItem item = new OrdenItem();
        ordenNueva.getItems().add(item);

        when(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER)).thenReturn(Optional.of(estadoSinHacer));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(ordenNueva)).thenReturn(ordenNueva);

        Orden resultado = ordenService.guardar(ordenNueva, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado).isSameAs(ordenNueva);
        assertThat(resultado.getFechaPedido()).isEqualTo(LocalDate.now());
        assertThat(resultado.getEstadoOrden()).isSameAs(estadoSinHacer);
        assertThat(item.getOrden()).isSameAs(ordenNueva);
        assertThat(item.getProducto()).isSameAs(producto);
        assertThat(resultado.getMovimientos()).hasSize(1);
        assertThat(resultado.getMovimientos().get(0).getOrden()).isSameAs(ordenNueva);

        verify(pagoService).crearPagoDesdeFormularioOrden(ordenNueva, ID_MEDIO_PAGO);
        verify(pagoService).actualizarEstadoPago(ordenNueva);
        verify(movimientoService).registrar(isNull(), same(empleado), isNull(), eq(OrigenMovimiento.FORMULARIO_CREACION));
        verify(ordenRepository).save(ordenNueva);
    }

    @Test
    void guardarCreaOrdenNuevaSinItemsCuandoLaListaEsNula() {
        Orden ordenNueva = orden(null, "Cliente", 1000, 0, null);
        ordenNueva.setItems(null);

        when(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER)).thenReturn(Optional.of(estadoSinHacer));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(ordenNueva)).thenReturn(ordenNueva);

        Orden resultado = ordenService.guardar(ordenNueva, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado.getItems()).isNull();
        verify(ordenRepository).save(ordenNueva);
    }

    @Test
    void guardarFallaAlCrearCuandoProductoNoExiste() {
        Orden ordenNueva = orden(null, "Cliente", 1000, 0, null);

        when(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER)).thenReturn(Optional.of(estadoSinHacer));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenService.guardar(ordenNueva, ID_PRODUCTO, ID_MEDIO_PAGO, empleado))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado");

        verify(ordenRepository, never()).save(any());
    }

    @Test
    void guardarEditaOrdenExistenteSinCambioDeAbonado() {
        Orden persistida = orden(8L, "Cliente anterior", 1000, 200, estadoSinHacer);
        OrdenItem itemAnterior = new OrdenItem();
        persistida.getItems().add(itemAnterior);
        Orden modificada = orden(8L, "Cliente nuevo", 1500, 200, estadoSinHacer);
        OrdenItem itemNuevo = new OrdenItem();
        modificada.getItems().add(itemNuevo);

        when(ordenRepository.findById(8L)).thenReturn(Optional.of(persistida));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(persistida)).thenReturn(persistida);

        Orden resultado = ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(resultado).isSameAs(persistida);
        assertThat(resultado.getNombreCliente()).isEqualTo("Cliente nuevo");
        assertThat(resultado.getTelefonoCliente()).isEqualTo(modificada.getTelefonoCliente());
        assertThat(resultado.getTotal()).isEqualTo(1500);
        assertThat(resultado.getAbonado()).isEqualTo(200);
        assertThat(resultado.getItems()).containsExactly(itemNuevo);
        assertThat(itemNuevo.getOrden()).isSameAs(persistida);
        assertThat(itemNuevo.getProducto()).isSameAs(producto);

        verify(pagoService, never()).eliminarPagosAsociados(anyLong());
        verify(pagoService, never()).crearPagoDesdeFormularioOrden(any(), any());
        verify(pagoService).actualizarEstadoPago(persistida);
        verify(movimientoService).registrar(isNull(), same(empleado), isNull(), eq(OrigenMovimiento.FORMULARIO_EDICION));
    }

    @Test
    void guardarEditaOrdenExistenteConCambioDeAbonado() {
        Orden persistida = orden(8L, "Cliente anterior", 1000, 200, estadoSinHacer);
        Orden modificada = orden(8L, "Cliente nuevo", 1500, 700, estadoSinHacer);

        when(ordenRepository.findById(8L)).thenReturn(Optional.of(persistida));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(persistida)).thenReturn(persistida);

        ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(persistida.getAbonado()).isEqualTo(700);
        verify(pagoService).eliminarPagosAsociados(8L);
        verify(pagoService).crearPagoDesdeFormularioOrden(persistida, ID_MEDIO_PAGO);
        verify(pagoService).actualizarEstadoPago(persistida);
        verify(movimientoService).registrar(
                isNull(),
                same(empleado),
                eq("Se modifica abonado a: $700"),
                eq(OrigenMovimiento.FORMULARIO_EDICION)
        );
    }

    @Test
    void guardarCorrigeOrdenYVuelveAlEstadoPrevio() {
        Orden persistida = orden(8L, "Cliente anterior", 1000, 200, estadoCorreccion);
        persistida.setCorreccion("Cambiar medidas");
        persistida.setIdEstadoPrevio(2);
        Orden modificada = orden(8L, "Cliente nuevo", 1500, 500, estadoCorreccion);

        when(ordenRepository.findById(8L)).thenReturn(Optional.of(persistida));
        when(estadoOrdenRepository.findById(ID_ESTADO_EN_PROCESO)).thenReturn(Optional.of(estadoEnProceso));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(persistida)).thenReturn(persistida);

        ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(persistida.getEstadoOrden()).isSameAs(estadoEnProceso);
        assertThat(persistida.getCorreccion()).isNull();
        assertThat(persistida.getIdEstadoPrevio()).isNull();
        verify(movimientoService).registrar(
                isNull(),
                same(empleado),
                eq("El pedido de corrección era: Cambiar medidas. | Se modifica abonado a: $500"),
                eq(OrigenMovimiento.FORMULARIO_CORRECCION)
        );
    }

    @Test
    void guardarCorrigeOrdenYUsaSinHacerCuandoNoTieneEstadoPrevio() {
        Orden persistida = orden(8L, "Cliente anterior", 1000, 200, estadoCorreccion);
        persistida.setCorreccion("Cambiar texto");
        persistida.setIdEstadoPrevio(null);
        Orden modificada = orden(8L, "Cliente nuevo", 1000, 200, estadoCorreccion);

        when(ordenRepository.findById(8L)).thenReturn(Optional.of(persistida));
        when(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER)).thenReturn(Optional.of(estadoSinHacer));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(persistida)).thenReturn(persistida);

        ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(persistida.getEstadoOrden()).isSameAs(estadoSinHacer);
        verify(movimientoService).registrar(
                isNull(),
                same(empleado),
                eq("El pedido de corrección era: Cambiar texto."),
                eq(OrigenMovimiento.FORMULARIO_CORRECCION)
        );
    }

    @Test
    void guardarEditaOrdenExistenteSinItemsNuevosDejaListaVacia() {
        Orden persistida = orden(8L, "Cliente anterior", 1000, 200, estadoSinHacer);
        persistida.getItems().add(new OrdenItem());
        Orden modificada = orden(8L, "Cliente nuevo", 1000, 200, estadoSinHacer);
        modificada.setItems(null);

        when(ordenRepository.findById(8L)).thenReturn(Optional.of(persistida));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(ordenRepository.save(persistida)).thenReturn(persistida);

        ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado);

        assertThat(persistida.getItems()).isEmpty();
    }

    @Test
    void guardarFallaAlEditarCuandoOrdenNoExiste() {
        Orden modificada = orden(8L, "Cliente", 1000, 200, estadoSinHacer);
        when(ordenRepository.findById(8L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Orden no encontrada con id: 8");

        verifyNoInteractions(productoRepository);
        verify(ordenRepository, never()).save(any());
    }

    @Test
    void guardarFallaAlEditarCuandoProductoNoExiste() {
        Orden persistida = orden(8L, "Cliente anterior", 1000, 200, estadoSinHacer);
        Orden modificada = orden(8L, "Cliente nuevo", 1000, 200, estadoSinHacer);

        when(ordenRepository.findById(8L)).thenReturn(Optional.of(persistida));
        when(productoRepository.findById(ID_PRODUCTO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenService.guardar(modificada, ID_PRODUCTO, ID_MEDIO_PAGO, empleado))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado");

        verify(ordenRepository, never()).save(any());
    }

    @Test
    void buscarPorIdNombreClienteOTelefonoDelegaEnRepositorio() {
        List<Orden> ordenes = List.of(orden(1L, "Ana", 100, 0, estadoSinHacer));
        when(ordenRepository.buscarPorIdNombreClienteOTelefono("ana", 3L)).thenReturn(ordenes);

        List<Orden> resultado = ordenService.buscarPorIdNombreClienteOTelefono("ana", 3L);

        assertThat(resultado).isSameAs(ordenes);
    }

    @Test
    void buscarOrdenesConEstadoSegunRolDelegaEnRepositorio() {
        List<Orden> ordenes = List.of(orden(1L, "Ana", 100, 0, estadoSinHacer));
        when(ordenRepository.buscarOrdenesConEstadoSegunRol(1L, 3L)).thenReturn(ordenes);

        List<Orden> resultado = ordenService.buscarOrdenesConEstadoSegunRol(1L, 3L);

        assertThat(resultado).isSameAs(ordenes);
    }

    @Test
    void cambiarEstadoOrdenAsignaEncargadoCuandoPasaAEnProcesoYSeSolicita() {
        Orden orden = orden(8L, "Cliente", 1000, 0, estadoSinHacer);
        when(ordenRepository.findById(8L)).thenReturn(Optional.of(orden));
        when(estadoOrdenRepository.findById(ID_ESTADO_EN_PROCESO)).thenReturn(Optional.of(estadoEnProceso));

        ordenService.cambiarEstadoOrden(8L, ID_ESTADO_EN_PROCESO, true, empleado);

        assertThat(orden.getEstadoOrden()).isSameAs(estadoEnProceso);
        assertThat(orden.getEncargadoProduccion()).isSameAs(empleado);
        assertThat(orden.getMovimientos()).hasSize(1);
        verify(movimientoService).registrar(ID_ESTADO_EN_PROCESO, empleado, null, OrigenMovimiento.CAMBIO_ESTADO);
        verify(ordenRepository).save(orden);
    }

    @Test
    void cambiarEstadoOrdenNoAsignaEncargadoCuandoNoCorresponde() {
        Orden orden = orden(8L, "Cliente", 1000, 0, estadoSinHacer);
        when(ordenRepository.findById(8L)).thenReturn(Optional.of(orden));
        when(estadoOrdenRepository.findById(ID_ESTADO_SIN_HACER)).thenReturn(Optional.of(estadoSinHacer));

        ordenService.cambiarEstadoOrden(8L, ID_ESTADO_SIN_HACER, true, empleado);

        assertThat(orden.getEncargadoProduccion()).isNull();
        assertThat(orden.getEstadoOrden()).isSameAs(estadoSinHacer);
    }

    @Test
    void cambiarEstadoOrdenNoAsignaEncargadoCuandoNoSeSolicita() {
        Orden orden = orden(8L, "Cliente", 1000, 0, estadoSinHacer);
        when(ordenRepository.findById(8L)).thenReturn(Optional.of(orden));
        when(estadoOrdenRepository.findById(ID_ESTADO_EN_PROCESO)).thenReturn(Optional.of(estadoEnProceso));

        ordenService.cambiarEstadoOrden(8L, ID_ESTADO_EN_PROCESO, false, empleado);

        assertThat(orden.getEncargadoProduccion()).isNull();
        assertThat(orden.getEstadoOrden()).isSameAs(estadoEnProceso);
    }

    @Test
    void cambiarEstadoOrdenFallaCuandoEstadoNoExiste() {
        Orden orden = orden(8L, "Cliente", 1000, 0, estadoSinHacer);
        when(ordenRepository.findById(8L)).thenReturn(Optional.of(orden));
        when(estadoOrdenRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordenService.cambiarEstadoOrden(8L, 99L, false, empleado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Estado no encontrado: 99");

        verify(ordenRepository, never()).save(any());
    }

    @Test
    void enviarAColumnaCorreccionGuardaEstadoPrevioCorreccionYMovimiento() {
        Orden orden = orden(8L, "Cliente", 1000, 0, estadoEnProceso);
        when(ordenRepository.findById(8L)).thenReturn(Optional.of(orden));
        when(estadoOrdenRepository.findById(ID_ESTADO_CORRECCION)).thenReturn(Optional.of(estadoCorreccion));

        ordenService.enviarAColumnaCorreccion(8L, "Falta logo", empleado);

        assertThat(orden.getIdEstadoPrevio()).isEqualTo(2);
        assertThat(orden.getEstadoOrden()).isSameAs(estadoCorreccion);
        assertThat(orden.getCorreccion()).isEqualTo("Falta logo");
        assertThat(orden.getMovimientos()).hasSize(1);
        verify(movimientoService).registrar(ID_ESTADO_CORRECCION, empleado, "Falta logo", OrigenMovimiento.PEDIDO_CORRECCION);
        verify(ordenRepository).save(orden);
    }

    @Test
    void obtenerOrdenesPorSemanaBuscaDeLunesADomingoYOrdenaPorFechaYHora() {
        LocalDate lunes = LocalDate.of(2026, 5, 11);
        Orden martesTarde = orden(1L, "Uno", 100, 0, estadoSinHacer);
        martesTarde.setFechaEntrega(lunes.plusDays(1));
        martesTarde.setHoraEntrega("18:00");
        Orden lunesSinHora = orden(2L, "Dos", 100, 0, estadoSinHacer);
        lunesSinHora.setFechaEntrega(lunes);
        lunesSinHora.setHoraEntrega("");
        Orden lunesTemprano = orden(3L, "Tres", 100, 0, estadoSinHacer);
        lunesTemprano.setFechaEntrega(lunes);
        lunesTemprano.setHoraEntrega("9.30");

        when(ordenRepository.buscarPorSemanaYTipo(lunes, lunes.plusDays(6), "entrega", empleado.getRol().getId()))
                .thenReturn(List.of(martesTarde, lunesSinHora, lunesTemprano));

        List<Orden> resultado = ordenService.obtenerOrdenesPorSemana(lunes, "entrega", empleado);

        assertThat(resultado).containsExactly(lunesTemprano, lunesSinHora, martesTarde);
    }

    @Test
    void ordenarYFormatearOrdenaPorIdDescendenteParaMariCommunity() {
        Empleado mariCommunity = empleado(2L, "maricommunity", 3L);
        Orden primera = orden(1L, "Uno", 100, 0, estadoSinHacer);
        Orden tercera = orden(3L, "Tres", 100, 0, estadoSinHacer);
        Orden segunda = orden(2L, "Dos", 100, 0, estadoSinHacer);

        List<Orden> resultado = ordenService.ordenarYFormatear(List.of(primera, tercera, segunda), mariCommunity);

        assertThat(resultado).containsExactly(tercera, segunda, primera);
    }

    @Test
    void ordenarYFormatearEnviaHorasInvalidasAlFinalDelMismoDia() {
        Orden horaValida = orden(1L, "Uno", 100, 0, estadoSinHacer);
        horaValida.setHoraEntrega("10:15");
        Orden horaMayorANormalizar = orden(2L, "Dos", 100, 0, estadoSinHacer);
        horaMayorANormalizar.setHoraEntrega("29:80");
        Orden horaTexto = orden(3L, "Tres", 100, 0, estadoSinHacer);
        horaTexto.setHoraEntrega("sin hora");
        Orden horaNula = orden(4L, "Cuatro", 100, 0, estadoSinHacer);
        horaNula.setHoraEntrega(null);

        List<Orden> resultado = ordenService.ordenarYFormatear(
                List.of(horaTexto, horaNula, horaMayorANormalizar, horaValida),
                empleado
        );

        assertThat(resultado).containsExactly(horaValida, horaMayorANormalizar, horaTexto, horaNula);
    }

    private Orden orden(Long id, String nombreCliente, int total, int abonado, EstadoOrden estadoOrden) {
        Orden orden = new Orden();
        orden.setId(id);
        orden.setNombreCliente(nombreCliente);
        orden.setTelefonoCliente("123456");
        orden.setEsCuentaCorriente(false);
        orden.setFechaPedido(LocalDate.of(2026, 5, 1));
        orden.setFechaMuestra(LocalDate.of(2026, 5, 2));
        orden.setFechaEntrega(LocalDate.of(2026, 5, 3));
        orden.setHoraEntrega("10:00");
        orden.setNecesitaFactura(false);
        orden.setTotal(total);
        orden.setSubtotal(total);
        orden.setPrecioDisenio(0);
        orden.setAbonado(abonado);
        orden.setEstadoOrden(estadoOrden);
        orden.setEmpleado(empleado);
        orden.setItems(new ArrayList<>());
        orden.setPagos(new ArrayList<>());
        orden.setMovimientos(new ArrayList<>());
        return orden;
    }

    private EstadoOrden estadoOrden(Long id) {
        EstadoOrden estadoOrden = new EstadoOrden();
        estadoOrden.setId(id);
        estadoOrden.setEstadoDeOrden("Estado " + id);
        return estadoOrden;
    }

    private Producto producto(Integer id) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Producto " + id);
        return producto;
    }

    private Empleado empleado(Long id, String username, Long rolId) {
        Rol rol = new Rol();
        rol.setId(rolId);
        rol.setNombre("Rol " + rolId);

        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setUsername(username);
        empleado.setNombre("Empleado " + id);
        empleado.setRol(rol);
        return empleado;
    }
}
