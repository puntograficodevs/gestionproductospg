package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.repository.MedioPagoRepository;
import com.puntografico.puntografico.service.MedioPagoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MedioPagoService.class)
class MedioPagoServiceTest {

    @Autowired
    private MedioPagoService medioPagoService;

    @MockBean
    private MedioPagoRepository medioPagoRepository;

    @Test
    void buscarTodos_conMediosExistentes_devuelveTodosLosMedios() {
        List<MedioPago> medios = List.of(medioPago(1L, "Efectivo"), medioPago(2L, "Tarjeta"));
        when(medioPagoRepository.findAll()).thenReturn(medios);

        List<MedioPago> resultado = medioPagoService.buscarTodos();

        assertThat(resultado).isSameAs(medios);
    }

    @Test
    void buscarTodos_conRepositorioVacio_devuelveListaVacia() {
        when(medioPagoRepository.findAll()).thenReturn(List.of());

        List<MedioPago> resultado = medioPagoService.buscarTodos();

        assertThat(resultado).isEmpty();
    }

    @Test
    void buscarPorId_conMedioExistente_devuelveMedio() {
        MedioPago medioPago = medioPago(1L, "Efectivo");
        when(medioPagoRepository.findById(1L)).thenReturn(Optional.of(medioPago));

        MedioPago resultado = medioPagoService.buscarPorId(1L);

        assertThat(resultado).isSameAs(medioPago);
    }

    @Test
    void buscarPorId_conIdNulo_lanzaExcepcion() {
        assertThatThrownBy(() -> medioPagoService.buscarPorId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El id del medio de pago no puede venir nulo.");

        verifyNoInteractions(medioPagoRepository);
    }

    @Test
    void buscarPorId_conMedioInexistente_lanzaExcepcion() {
        when(medioPagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medioPagoService.buscarPorId(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    private MedioPago medioPago(Long id, String nombre) {
        MedioPago medioPago = new MedioPago();
        medioPago.setId(id);
        medioPago.setMedioDePago(nombre);
        return medioPago;
    }
}
