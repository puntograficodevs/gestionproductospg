package com.puntografico.puntografico.integration;

import com.puntografico.puntografico.controller.BuscadorController;
import com.puntografico.puntografico.controller.FacturaController;
import com.puntografico.puntografico.controller.HomeController;
import com.puntografico.puntografico.controller.ListadoController;
import com.puntografico.puntografico.controller.LoginController;
import com.puntografico.puntografico.controller.MovimientoController;
import com.puntografico.puntografico.controller.OrdenController;
import com.puntografico.puntografico.controller.rest.ProductoCatalogoRestController;
import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.EstadoOrden;
import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.domain.Movimiento;
import com.puntografico.puntografico.domain.Orden;
import com.puntografico.puntografico.domain.OrdenItem;
import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.domain.ProductoCatalogo;
import com.puntografico.puntografico.domain.Rol;
import com.puntografico.puntografico.handler.LoginInterceptor;
import com.puntografico.puntografico.handler.WebConfig;
import com.puntografico.puntografico.repository.OrdenRepository;
import com.puntografico.puntografico.service.EmpleadoService;
import com.puntografico.puntografico.service.MedioPagoService;
import com.puntografico.puntografico.service.MovimientoService;
import com.puntografico.puntografico.service.OrdenService;
import com.puntografico.puntografico.service.PagoService;
import com.puntografico.puntografico.service.ProductoCatalogoService;
import com.puntografico.puntografico.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = {
                LoginController.class,
                HomeController.class,
                OrdenController.class,
                ListadoController.class,
                BuscadorController.class,
                MovimientoController.class,
                FacturaController.class,
                ProductoCatalogoRestController.class
        },
        properties = "spring.thymeleaf.enabled=false"
)
@Import({WebConfig.class, LoginInterceptor.class, MvcIntegrationTest.TestViewConfig.class})
class MvcIntegrationTest {

    private static final long ID_ORDEN = 10L;
    private static final int ID_PRODUCTO = 20;
    private static final long ID_MEDIO_PAGO = 30L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private OrdenService ordenService;

    @MockBean
    private MedioPagoService medioPagoService;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ProductoCatalogoService productoCatalogoService;

    @MockBean
    private MovimientoService movimientoService;

    @MockBean
    private OrdenRepository ordenRepository;

    @Test
    void rutaProtegida_sinEmpleadoLogueado_redirigeALogin() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void login_conCredencialesValidas_guardaEmpleadoEnSesionYRedirigeHome() throws Exception {
        Empleado empleado = empleado();
        when(empleadoService.validarEmpleado("usuario", "clave")).thenReturn(true);
        when(empleadoService.traerEmpleadoPorUsername("usuario")).thenReturn(empleado);

        mockMvc.perform(post("/login")
                        .param("username", "usuario")
                        .param("password", "clave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(request().sessionAttribute("empleadoLogueado", empleado));
    }

    @Test
    void login_conCredencialesInvalidas_devuelveLoginConError() throws Exception {
        when(empleadoService.validarEmpleado("usuario", "mala")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("username", "usuario")
                        .param("password", "mala"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", true));
    }

    @Test
    void logout_conSesionIniciada_invalidaSesionYRedirigeLogin() throws Exception {
        mockMvc.perform(get("/logout").sessionAttr("empleadoLogueado", empleado()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(request().sessionAttributeDoesNotExist("empleadoLogueado"));
    }

    @Test
    void home_conEmpleadoLogueado_cargaVistaConEmpleado() throws Exception {
        Empleado empleado = empleado();

        mockMvc.perform(get("/home").sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("empleado", empleado));
    }

    @Test
    void nuevaOrden_conEmpleadoLogueado_cargaProductosOrdenYEmpleado() throws Exception {
        Empleado empleado = empleado();
        List<Producto> productos = List.of(producto("Producto uno"), producto("Producto dos"));
        when(productoService.traerTodosLosProductosOrdenadosPorNombre()).thenReturn(productos);

        mockMvc.perform(get("/ordenes/nueva-orden").sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("nueva-orden"))
                .andExpect(model().attribute("empleado", empleado))
                .andExpect(model().attributeExists("orden"))
                .andExpect(model().attribute("productos", productos));
    }

    @Test
    void guardarOrden_conItemValido_serializaDetalleGuardaYRedirigeExito() throws Exception {
        Empleado empleado = empleado();
        Orden ordenGuardada = orden(ID_ORDEN);
        when(ordenService.guardar(any(Orden.class), eq(ID_PRODUCTO), eq(ID_MEDIO_PAGO), eq(empleado)))
                .thenReturn(ordenGuardada);

        mockMvc.perform(post("/ordenes/guardar-orden")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("productoId", String.valueOf(ID_PRODUCTO))
                        .param("idMedioPago", String.valueOf(ID_MEDIO_PAGO))
                        .param("nombreCliente", "Cliente")
                        .param("telefonoCliente", "123")
                        .param("fechaEntrega", "2026-06-05")
                        .param("horaEntrega", "10:00")
                        .param("total", "8000")
                        .param("subtotal", "8000")
                        .param("abonado", "500")
                        .param("items[0].cantidad", "1")
                        .param("items[0].precioUnitario", "8000")
                        .param("items[0].detalles[tipo]", "Color"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ordenes/exito/" + ID_ORDEN));

        ArgumentCaptor<Orden> captorOrden = ArgumentCaptor.forClass(Orden.class);
        verify(ordenService).guardar(captorOrden.capture(), eq(ID_PRODUCTO), eq(ID_MEDIO_PAGO), eq(empleado));
        assertThat(captorOrden.getValue().getItems()).hasSize(1);
        assertThat(captorOrden.getValue().getItems().get(0).getDetallePersonalizado())
                .contains("\"tipo\":\"Color\"");
    }

    @Test
    void guardarOrden_conServiceLanzaExcepcion_redirigeError() throws Exception {
        Empleado empleado = empleado();
        when(ordenService.guardar(any(Orden.class), eq(ID_PRODUCTO), eq(ID_MEDIO_PAGO), eq(empleado)))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/ordenes/guardar-orden")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("productoId", String.valueOf(ID_PRODUCTO))
                        .param("idMedioPago", String.valueOf(ID_MEDIO_PAGO))
                        .param("nombreCliente", "Cliente")
                        .param("telefonoCliente", "123")
                        .param("fechaEntrega", "2026-06-05")
                        .param("horaEntrega", "10:00")
                        .param("total", "8000")
                        .param("subtotal", "8000")
                        .param("abonado", "500"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ordenes/error"));
    }

    @Test
    void formularioProducto_conProductoExistente_cargaFragmentoPrincipal() throws Exception {
        Empleado empleado = empleado();
        Producto producto = producto("Copias");
        producto.setId(ID_PRODUCTO);
        producto.setEsquemaConfiguracion("[{\"nombre\":\"Tamanio\"}]");
        List<MedioPago> medios = List.of(medioPago("Efectivo"));
        List<ProductoCatalogo> materiales = new ArrayList<>(List.of(material("Obra")));

        when(productoService.buscarPorId(ID_PRODUCTO)).thenReturn(Optional.of(producto));
        when(medioPagoService.buscarTodos()).thenReturn(medios);
        when(productoCatalogoService.buscarTodasLasCopiasEscolaresEnCatalogo()).thenReturn(materiales);

        mockMvc.perform(get("/ordenes/formulario-producto/" + ID_PRODUCTO)
                        .sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("fragments/formulario-dinamico :: cuerpo-formulario"))
                .andExpect(model().attribute("producto", producto))
                .andExpect(model().attribute("listaMediosDePago", medios))
                .andExpect(model().attribute("listaMateriales", materiales))
                .andExpect(model().attribute("empleado", empleado))
                .andExpect(model().attribute("index", 0));
    }

    @Test
    void formularioProducto_conIndexMayorACero_cargaFragmentoDeItem() throws Exception {
        Producto producto = producto("Copias");
        producto.setId(ID_PRODUCTO);
        producto.setEsquemaConfiguracion("json invalido");
        when(productoService.buscarPorId(ID_PRODUCTO)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/ordenes/formulario-producto/" + ID_PRODUCTO)
                        .sessionAttr("empleadoLogueado", empleado())
                        .param("index", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("fragments/formulario-dinamico :: bloque-item-card"))
                .andExpect(model().attribute("index", 2));
    }

    @Test
    void exito_conOrdenExistente_formateaFechasYCargaOrden() throws Exception {
        Empleado empleado = empleado();
        Orden orden = orden(ID_ORDEN);
        orden.setFechaPedido(LocalDate.of(2026, 6, 1));
        orden.setFechaEntrega(LocalDate.of(2026, 6, 5));
        orden.setFechaMuestra(LocalDate.of(2026, 6, 20));
        when(ordenService.buscarPorId(ID_ORDEN)).thenReturn(orden);

        mockMvc.perform(get("/ordenes/exito/" + ID_ORDEN).sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("exito"))
                .andExpect(model().attribute("empleado", empleado))
                .andExpect(model().attribute("ordenTrabajo", orden))
                .andExpect(model().attribute("fechaPedido", "01/06/2026"))
                .andExpect(model().attribute("fechaEntrega", "05/06/2026"))
                .andExpect(model().attribute("fechaMuestra", "20/06/2026"));
    }

    @Test
    void detalleFragmento_conOrdenExistente_cargaOrdenYMedios() throws Exception {
        Orden orden = orden(ID_ORDEN);
        List<MedioPago> medios = List.of(medioPago("Efectivo"));
        when(ordenService.buscarPorId(ID_ORDEN)).thenReturn(orden);
        when(medioPagoService.buscarTodos()).thenReturn(medios);

        mockMvc.perform(get("/ordenes/detalle-fragmento/" + ID_ORDEN).sessionAttr("empleadoLogueado", empleado()))
                .andExpect(status().isOk())
                .andExpect(view().name("exito :: detalle-orden"))
                .andExpect(model().attribute("orden", orden))
                .andExpect(model().attribute("listaMediosDePago", medios));
    }

    @Test
    void editarOrden_conOrdenExistente_cargaDatosParaFormulario() throws Exception {
        Empleado empleado = empleado();
        Producto producto = producto("Producto");
        producto.setEsquemaConfiguracion("[{\"nombre\":\"Tamanio\"}]");
        Orden orden = orden(ID_ORDEN);
        OrdenItem item = new OrdenItem();
        item.setProducto(producto);
        orden.getItems().add(item);
        List<MedioPago> medios = List.of(medioPago("Efectivo"));
        List<ProductoCatalogo> materiales = List.of(material("Obra"));

        when(ordenService.buscarPorId(ID_ORDEN)).thenReturn(orden);
        when(medioPagoService.buscarTodos()).thenReturn(medios);
        when(productoCatalogoService.buscarTodasLasCopiasEscolaresEnCatalogo()).thenReturn(materiales);

        mockMvc.perform(get("/ordenes/editar-orden/" + ID_ORDEN).sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("nueva-orden"))
                .andExpect(model().attribute("orden", orden))
                .andExpect(model().attribute("producto", producto))
                .andExpect(model().attribute("empleado", empleado))
                .andExpect(model().attribute("listaMediosDePago", medios))
                .andExpect(model().attribute("listaMateriales", materiales))
                .andExpect(model().attribute("esEdicion", true));
    }

    @Test
    void eliminarOrden_conId_eliminaYRedirigeBuscador() throws Exception {
        mockMvc.perform(get("/ordenes/eliminar/" + ID_ORDEN).sessionAttr("empleadoLogueado", empleado()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscador"));

        verify(ordenService).eliminar(ID_ORDEN);
    }

    @Test
    void eliminarVarias_conIdsValidosEInvalidos_eliminaSoloIdsValidos() throws Exception {
        mockMvc.perform(post("/ordenes/eliminar-varias")
                        .sessionAttr("empleadoLogueado", empleado())
                        .param("ids", "10, abc, 11"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscador"));

        verify(ordenService).eliminar(10L);
        verify(ordenService).eliminar(11L);
        verify(ordenService, never()).eliminar(0L);
    }

    @Test
    void pasarRetiradaDesdeBuscador_conOrden_llamaCambioEstadoYRedirigeBuscador() throws Exception {
        Empleado empleado = empleado();

        mockMvc.perform(get("/ordenes/pasar-retirada-desde-buscador/" + ID_ORDEN)
                        .sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscador?idOrden=" + ID_ORDEN));

        verify(ordenService).cambiarEstadoOrden(ID_ORDEN, 5L, false, empleado);
    }

    @Test
    void cambiarEstado_conParametros_llamaServiceYRedirigeListadoConFiltro() throws Exception {
        Empleado empleado = empleado();
        String producto = "Impresi\u00f3n con dise\u00f1o";

        mockMvc.perform(get("/ordenes/cambiar-estado/" + ID_ORDEN)
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("producto", producto)
                        .param("nuevoEstado", "2")
                        .param("asignarEncargado", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listado?producto=Impresi%C3%B3n+con+dise%C3%B1o"));

        verify(ordenService).cambiarEstadoOrden(ID_ORDEN, 2L, true, empleado);
    }

    @Test
    void enviarACorregir_conMotivo_llamaServiceYRedirigeListado() throws Exception {
        Empleado empleado = empleado();

        mockMvc.perform(post("/ordenes/enviar-a-corregir")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("id", String.valueOf(ID_ORDEN))
                        .param("motivo", "Falta archivo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listado"));

        verify(ordenService).enviarAColumnaCorreccion(ID_ORDEN, "Falta archivo", empleado);
    }

    @Test
    void registrarPago_desdeListado_registraPagoYRedirigeListado() throws Exception {
        Empleado empleado = empleado();

        mockMvc.perform(post("/ordenes/registrar-pago")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("ordenId", String.valueOf(ID_ORDEN))
                        .param("importe", "1000")
                        .param("idMedioPago", String.valueOf(ID_MEDIO_PAGO)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listado"));

        verify(pagoService).registrarPagoExtra(ID_ORDEN, 1000, ID_MEDIO_PAGO, empleado);
    }

    @Test
    void registrarPago_desdeModal_registraPagoYRedirigeBuscadorConOrden() throws Exception {
        Empleado empleado = empleado();

        mockMvc.perform(post("/ordenes/registrar-pago")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("ordenId", String.valueOf(ID_ORDEN))
                        .param("importe", "1000")
                        .param("idMedioPago", String.valueOf(ID_MEDIO_PAGO))
                        .param("desdeModal", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscador?idOrden=" + ID_ORDEN));

        verify(pagoService).registrarPagoExtra(ID_ORDEN, 1000, ID_MEDIO_PAGO, empleado);
    }

    @Test
    void calendario_conLunesYTipoMuestra_cargaDatosSemana() throws Exception {
        Empleado empleado = empleado();
        LocalDate lunes = LocalDate.of(2026, 6, 1);
        Orden orden = orden(ID_ORDEN);
        orden.setFechaMuestra(LocalDate.of(2026, 6, 2));
        List<Orden> ordenes = List.of(orden);
        when(ordenService.obtenerOrdenesPorSemana(lunes, "muestra", empleado)).thenReturn(ordenes);

        mockMvc.perform(get("/ordenes/calendario")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("tipo", "muestra")
                        .param("lunes", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("calendario"))
                .andExpect(model().attributeExists("datos"))
                .andExpect(model().attribute("lunesActual", lunes))
                .andExpect(model().attribute("tipoActual", "muestra"))
                .andExpect(model().attribute("semanaPrevia", LocalDate.of(2026, 5, 25)))
                .andExpect(model().attribute("semanaSiguiente", LocalDate.of(2026, 6, 8)))
                .andExpect(model().attribute("empleado", empleado));
    }

    @Test
    void calendario_sinLunes_usaLunesDeSemanaActual() throws Exception {
        Empleado empleado = empleado();
        LocalDate lunesActual = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        when(ordenService.obtenerOrdenesPorSemana(lunesActual, "entrega", empleado)).thenReturn(List.of());

        mockMvc.perform(get("/ordenes/calendario").sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("calendario"))
                .andExpect(model().attribute("lunesActual", lunesActual))
                .andExpect(model().attribute("tipoActual", "entrega"));
    }

    @Test
    void listado_conFiltro_cargaColumnasMediosProductosYEmpleado() throws Exception {
        Empleado empleado = empleado();
        List<Orden> ordenesSinHacer = List.of(orden(1L));
        List<Orden> ordenesEnProceso = List.of(orden(2L));
        List<Orden> ordenesListaParaRetirar = List.of(orden(3L));
        List<Orden> ordenesCorregir = List.of(orden(4L));
        List<MedioPago> medios = List.of(medioPago("Efectivo"));
        Producto producto = producto("Copias");
        Producto sinCategoria = producto("Sin categoria");

        when(ordenService.buscarOrdenesConEstadoSegunRol(1L, empleado.getRol().getId())).thenReturn(ordenesSinHacer);
        when(ordenService.buscarOrdenesConEstadoSegunRol(2L, empleado.getRol().getId())).thenReturn(ordenesEnProceso);
        when(ordenService.buscarOrdenesConEstadoSegunRol(3L, empleado.getRol().getId())).thenReturn(ordenesListaParaRetirar);
        when(ordenService.buscarOrdenesConEstadoSegunRol(4L, empleado.getRol().getId())).thenReturn(ordenesCorregir);
        when(ordenService.ordenarYFormatear(any(), eq(empleado))).thenAnswer(invocation -> invocation.getArgument(0));
        when(medioPagoService.buscarTodos()).thenReturn(medios);
        when(productoService.buscarTodos()).thenReturn(List.of(sinCategoria, producto));

        mockMvc.perform(get("/listado")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("producto", "copias"))
                .andExpect(status().isOk())
                .andExpect(view().name("listado"))
                .andExpect(model().attribute("filtroSeleccionado", "copias"))
                .andExpect(model().attribute("ordenesSinHacer", ordenesSinHacer))
                .andExpect(model().attribute("ordenesEnProceso", ordenesEnProceso))
                .andExpect(model().attribute("ordenesListaParaRetirar", ordenesListaParaRetirar))
                .andExpect(model().attribute("ordenesCorregir", ordenesCorregir))
                .andExpect(model().attribute("listaMediosDePago", medios))
                .andExpect(model().attribute("empleado", empleado));
    }

    @Test
    void buscadorGet_conIdOrden_cargaOrdenEncontrada() throws Exception {
        Empleado empleado = empleado();
        Orden orden = orden(ID_ORDEN);
        when(ordenService.buscarPorId(ID_ORDEN)).thenReturn(orden);

        mockMvc.perform(get("/buscador")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("idOrden", String.valueOf(ID_ORDEN)))
                .andExpect(status().isOk())
                .andExpect(view().name("buscador"))
                .andExpect(model().attribute("ordenesEncontradas", List.of(orden)))
                .andExpect(model().attribute("empleado", empleado));
    }

    @Test
    void buscarOrden_conDatoVacio_noBuscaYCargaListaVacia() throws Exception {
        Empleado empleado = empleado();

        mockMvc.perform(post("/buscar-orden")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("datoOrden", " "))
                .andExpect(status().isOk())
                .andExpect(view().name("buscador"))
                .andExpect(model().attribute("ordenesEncontradas", List.of()))
                .andExpect(model().attribute("datoBusqueda", " "));

        verify(ordenService, never()).buscarPorIdNombreClienteOTelefono(any(), anyLong());
    }

    @Test
    void buscarOrden_conDatoValido_buscaPorRolYCargaResultados() throws Exception {
        Empleado empleado = empleado();
        List<Orden> ordenes = List.of(orden(ID_ORDEN));
        when(ordenService.buscarPorIdNombreClienteOTelefono("Cliente", empleado.getRol().getId())).thenReturn(ordenes);

        mockMvc.perform(post("/buscar-orden")
                        .sessionAttr("empleadoLogueado", empleado)
                        .param("datoOrden", "Cliente"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscador"))
                .andExpect(model().attribute("ordenesEncontradas", ordenes))
                .andExpect(model().attribute("datoBusqueda", "Cliente"));
    }

    @Test
    void historialMovimientos_conOrden_cargaFragmentoConMovimientos() throws Exception {
        List<Movimiento> movimientos = List.of(new Movimiento(), new Movimiento());
        when(movimientoService.buscarPorOrden(ID_ORDEN)).thenReturn(movimientos);

        mockMvc.perform(get("/ordenes/" + ID_ORDEN + "/historial").sessionAttr("empleadoLogueado", empleado()))
                .andExpect(status().isOk())
                .andExpect(view().name("fragments/historial-movimientos :: historialMovimientos"))
                .andExpect(model().attribute("movimientos", movimientos))
                .andExpect(model().attribute("idOrden", ID_ORDEN));
    }

    @Test
    void facturasGet_conEmpleadoLogueado_cargaPendientesSegunRol() throws Exception {
        Empleado empleado = empleado();
        List<Orden> pendientes = List.of(orden(ID_ORDEN));
        when(ordenRepository.buscarFacturasPendientesSegunRol(empleado.getRol().getId())).thenReturn(pendientes);

        mockMvc.perform(get("/facturas").sessionAttr("empleadoLogueado", empleado))
                .andExpect(status().isOk())
                .andExpect(view().name("facturas-pendientes"))
                .andExpect(model().attribute("ordenes", pendientes))
                .andExpect(model().attribute("empleado", empleado));
    }

    @Test
    void marcarFacturaHecha_conOrdenExistente_marcaGuardaYRedirigeFacturas() throws Exception {
        Orden orden = orden(ID_ORDEN);
        when(ordenRepository.findById(ID_ORDEN)).thenReturn(Optional.of(orden));

        mockMvc.perform(post("/facturas/marcar-hecha/" + ID_ORDEN).sessionAttr("empleadoLogueado", empleado()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/facturas"));

        assertThat(orden.isFacturaHecha()).isTrue();
        verify(ordenRepository).save(orden);
    }

    @Test
    void apiBuscarPrecio_conPrecioCoincidente_devuelvePrecio() throws Exception {
        when(productoCatalogoService.buscarPrecioCoincidente(eq(ID_PRODUCTO), anyMap())).thenReturn(1234);

        mockMvc.perform(post("/api/catalogo/buscar-precio")
                        .sessionAttr("empleadoLogueado", empleado())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productoId\":20,\"detalles\":{\"tipo\":\"Color\"}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("1234"));
    }

    @Test
    void apiBuscarPrecio_sinPrecioCoincidente_devuelveCero() throws Exception {
        when(productoCatalogoService.buscarPrecioCoincidente(eq(ID_PRODUCTO), anyMap())).thenReturn(null);

        mockMvc.perform(post("/api/catalogo/buscar-precio")
                        .sessionAttr("empleadoLogueado", empleado())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productoId\":20,\"detalles\":{\"tipo\":\"Color\"}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void apiBuscarPrecio_sinSesion_redirigeLoginPorInterceptor() throws Exception {
        mockMvc.perform(post("/api/catalogo/buscar-precio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productoId\":20,\"detalles\":{}}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verifyNoInteractions(productoCatalogoService);
    }

    @Test
    void recursoEstatico_sinSesion_noEsInterceptadoPorLogin() throws Exception {
        mockMvc.perform(get("/css/root.css"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestViewConfig {

        @Bean
        ViewResolver viewResolver() {
            return (viewName, locale) -> {
                if (viewName.startsWith("redirect:")) {
                    return null;
                }

                return new AbstractView() {
                    @Override
                    protected void renderMergedOutputModel(
                            Map<String, Object> model,
                            HttpServletRequest request,
                            HttpServletResponse response
                    ) {
                        response.setContentType(MediaType.TEXT_HTML_VALUE);
                    }
                };
            };
        }
    }

    private Empleado empleado() {
        Rol rol = new Rol();
        rol.setId(3L);
        rol.setNombre("Administracion");

        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Empleado");
        empleado.setUsername("usuario");
        empleado.setRol(rol);
        return empleado;
    }

    private Orden orden(Long id) {
        Orden orden = new Orden();
        orden.setId(id);
        orden.setNombreCliente("Cliente");
        orden.setTelefonoCliente("123");
        orden.setFechaPedido(LocalDate.of(2026, 6, 1));
        orden.setFechaEntrega(LocalDate.of(2026, 6, 5));
        orden.setHoraEntrega("10:00");
        orden.setTotal(8000);
        orden.setSubtotal(8000);
        orden.setAbonado(0);
        orden.setEstadoOrden(estadoOrden(1L));
        orden.setItems(new ArrayList<>());
        orden.setPagos(new ArrayList<>());
        orden.setMovimientos(new ArrayList<>());
        return orden;
    }

    private Producto producto(String nombre) {
        Producto producto = new Producto();
        producto.setId(ID_PRODUCTO);
        producto.setNombre(nombre);
        producto.setEsquemaConfiguracion("[]");
        return producto;
    }

    private MedioPago medioPago(String nombre) {
        MedioPago medioPago = new MedioPago();
        medioPago.setId(ID_MEDIO_PAGO);
        medioPago.setMedioDePago(nombre);
        return medioPago;
    }

    private ProductoCatalogo material(String nombre) {
        ProductoCatalogo productoCatalogo = new ProductoCatalogo();
        productoCatalogo.setNombreNegocio(nombre);
        return productoCatalogo;
    }

    private EstadoOrden estadoOrden(Long id) {
        EstadoOrden estadoOrden = new EstadoOrden();
        estadoOrden.setId(id);
        estadoOrden.setEstadoDeOrden("Estado");
        return estadoOrden;
    }
}
