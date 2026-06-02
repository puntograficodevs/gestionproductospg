package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.ProductoCatalogo;
import com.puntografico.puntografico.repository.ProductoCatalogoRepostory;
import com.puntografico.puntografico.service.ProductoCatalogoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ProductoCatalogoService.class)
class ProductoCatalogoServiceTest {

    private static final Integer ID_PRODUCTO = 10;
    private static final Integer ID_COPIAS_ESCOLARES = 34;

    @Autowired
    private ProductoCatalogoService productoCatalogoService;

    @MockBean
    private ProductoCatalogoRepostory productoCatalogoRepository;

    @Test
    void buscarPrecioCoincidente_conDetallesExactos_devuelvePrecio() {
        ProductoCatalogo variante = productoCatalogo(1L, 1500, "{\"papel\":\"OBRA\",\"faz\":\"SIMPLE\"}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(variante));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(
                ID_PRODUCTO,
                Map.of("papel", "OBRA", "faz", "SIMPLE")
        );

        assertThat(resultado).isEqualTo(1500);
    }

    @Test
    void buscarPrecioCoincidente_conDetallesSeleccionadosExtra_devuelvePrecio() {
        ProductoCatalogo variante = productoCatalogo(1L, 1500, "{\"papel\":\"OBRA\"}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(variante));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(
                ID_PRODUCTO,
                Map.of("papel", "OBRA", "faz", "SIMPLE")
        );

        assertThat(resultado).isEqualTo(1500);
    }

    @Test
    void buscarPrecioCoincidente_conPrimeraVarianteSinCoincidenciaYSegundaCoincidente_devuelvePrecioDeSegunda() {
        ProductoCatalogo primera = productoCatalogo(1L, 1000, "{\"papel\":\"OPALINA\"}");
        ProductoCatalogo segunda = productoCatalogo(2L, 1500, "{\"papel\":\"OBRA\"}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(primera, segunda));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(ID_PRODUCTO, Map.of("papel", "OBRA"));

        assertThat(resultado).isEqualTo(1500);
    }

    @Test
    void buscarPrecioCoincidente_conDetallesInsuficientes_devuelveNull() {
        ProductoCatalogo variante = productoCatalogo(1L, 1500, "{\"papel\":\"OBRA\",\"faz\":\"SIMPLE\"}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(variante));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(ID_PRODUCTO, Map.of("papel", "OBRA"));

        assertThat(resultado).isNull();
    }

    @Test
    void buscarPrecioCoincidente_conValorDistinto_devuelveNull() {
        ProductoCatalogo variante = productoCatalogo(1L, 1500, "{\"papel\":\"OBRA\"}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(variante));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(ID_PRODUCTO, Map.of("papel", "OPALINA"));

        assertThat(resultado).isNull();
    }

    @Test
    void buscarPrecioCoincidente_conCatalogoVacio_devuelveNull() {
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of());

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(ID_PRODUCTO, Map.of("papel", "OBRA"));

        assertThat(resultado).isNull();
    }

    @Test
    void buscarPrecioCoincidente_conJsonInvalidoLoIgnoraYDevuelveSiguienteCoincidencia() {
        ProductoCatalogo invalida = productoCatalogo(1L, 1000, "{json-invalido");
        ProductoCatalogo valida = productoCatalogo(2L, 1500, "{\"papel\":\"OBRA\"}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(invalida, valida));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(ID_PRODUCTO, Map.of("papel", "OBRA"));

        assertThat(resultado).isEqualTo(1500);
    }

    @Test
    void buscarPrecioCoincidente_conMapaVarianteVacio_devuelvePrecio() {
        ProductoCatalogo variante = productoCatalogo(1L, 1500, "{}");
        when(productoCatalogoRepository.findByProductoId(ID_PRODUCTO)).thenReturn(List.of(variante));

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(ID_PRODUCTO, Map.of("papel", "OBRA"));

        assertThat(resultado).isEqualTo(1500);
    }

    @Test
    void buscarPrecioCoincidente_conIdProductoNulo_delegaEnRepositorioYDevuelveNullSiNoHayVariantes() {
        when(productoCatalogoRepository.findByProductoId(null)).thenReturn(List.of());

        Integer resultado = productoCatalogoService.buscarPrecioCoincidente(null, Map.of("papel", "OBRA"));

        assertThat(resultado).isNull();
    }

    @Test
    void buscarTodasLasCopiasEscolaresEnCatalogo_conVariantesExistentes_devuelveCatalogoDeCopiasEscolares() {
        List<ProductoCatalogo> variantes = List.of(productoCatalogo(1L, 1500, "{\"escuela\":\"A\"}"));
        when(productoCatalogoRepository.findByProductoId(ID_COPIAS_ESCOLARES)).thenReturn(variantes);

        List<ProductoCatalogo> resultado = productoCatalogoService.buscarTodasLasCopiasEscolaresEnCatalogo();

        assertThat(resultado).isSameAs(variantes);
    }

    @Test
    void buscarTodasLasCopiasEscolaresEnCatalogo_conCatalogoVacio_devuelveListaVacia() {
        when(productoCatalogoRepository.findByProductoId(ID_COPIAS_ESCOLARES)).thenReturn(List.of());

        List<ProductoCatalogo> resultado = productoCatalogoService.buscarTodasLasCopiasEscolaresEnCatalogo();

        assertThat(resultado).isEmpty();
    }

    private ProductoCatalogo productoCatalogo(Long id, Integer precio, String detallesProducto) {
        ProductoCatalogo productoCatalogo = new ProductoCatalogo();
        productoCatalogo.setId(id);
        productoCatalogo.setPrecio(precio);
        productoCatalogo.setDetallesProducto(detallesProducto);
        return productoCatalogo;
    }
}
