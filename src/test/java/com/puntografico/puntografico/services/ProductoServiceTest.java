package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.repository.ProductoRepository;
import com.puntografico.puntografico.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ProductoService.class)
class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @MockBean
    private ProductoRepository productoRepository;

    @Test
    void buscarTodos_conProductosExistentes_devuelveTodosLosProductos() {
        List<Producto> productos = List.of(producto(1, "Sello"), producto(2, "Vinilo"));
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.buscarTodos();

        assertThat(resultado).isSameAs(productos);
    }

    @Test
    void buscarTodos_conRepositorioVacio_devuelveListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = productoService.buscarTodos();

        assertThat(resultado).isEmpty();
    }

    @Test
    void buscarPorId_conProductoExistente_devuelveProducto() {
        Producto producto = producto(1, "Sello");
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.buscarPorId(1);

        assertThat(resultado).containsSame(producto);
    }

    @Test
    void buscarPorId_conProductoInexistente_devuelveOptionalVacio() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.buscarPorId(99);

        assertThat(resultado).isEmpty();
    }

    @Test
    void traerTodosLosProductosOrdenadosPorNombre_conProductosDesordenados_ordenaPorNombreYDejaSinCategoriaAlFinal() {
        Producto sinCategoria = producto(1, "Sin categoria");
        Producto vinilo = producto(2, "Vinilo");
        Producto anillado = producto(3, "Anillado");
        Producto copia = producto(4, "copia escolar");
        when(productoRepository.findAll()).thenReturn(List.of(sinCategoria, vinilo, anillado, copia));

        List<Producto> resultado = productoService.traerTodosLosProductosOrdenadosPorNombre();

        assertThat(resultado).containsExactly(anillado, copia, vinilo, sinCategoria);
    }

    @Test
    void traerTodosLosProductosOrdenadosPorNombre_conSinCategoriaConDistintaCapitalizacion_laDejaAlFinal() {
        Producto sinCategoria = producto(1, "SIN CATEGORIA");
        Producto etiqueta = producto(2, "Etiqueta");
        when(productoRepository.findAll()).thenReturn(List.of(sinCategoria, etiqueta));

        List<Producto> resultado = productoService.traerTodosLosProductosOrdenadosPorNombre();

        assertThat(resultado).containsExactly(etiqueta, sinCategoria);
    }

    @Test
    void traerTodosLosProductosOrdenadosPorNombre_conListaVacia_devuelveListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = productoService.traerTodosLosProductosOrdenadosPorNombre();

        assertThat(resultado).isEmpty();
    }

    private Producto producto(Integer id, String nombre) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        return producto;
    }
}
