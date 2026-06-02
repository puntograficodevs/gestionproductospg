package com.puntografico.puntografico.services;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.repository.EmpleadoRepository;
import com.puntografico.puntografico.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = EmpleadoService.class)
class EmpleadoServiceTest {

    @Autowired
    private EmpleadoService empleadoService;

    @MockBean
    private EmpleadoRepository empleadoRepository;

    @Test
    void validarEmpleado_conCredencialesExistentes_devuelveTrue() {
        Empleado empleado = empleado("usuario");
        when(empleadoRepository.findByUsernameAndPassword("usuario", "clave")).thenReturn(Optional.of(empleado));

        boolean resultado = empleadoService.validarEmpleado("usuario", "clave");

        assertThat(resultado).isTrue();
    }

    @Test
    void validarEmpleado_conCredencialesInexistentes_devuelveFalse() {
        when(empleadoRepository.findByUsernameAndPassword("usuario", "clave")).thenReturn(Optional.empty());

        boolean resultado = empleadoService.validarEmpleado("usuario", "clave");

        assertThat(resultado).isFalse();
    }

    @Test
    void validarEmpleado_conUsernameNulo_lanzaExcepcion() {
        assertThatThrownBy(() -> empleadoService.validarEmpleado(null, "clave"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El username no puede venir nulo.");

        verifyNoInteractions(empleadoRepository);
    }

    @Test
    void validarEmpleado_conPasswordNula_lanzaExcepcion() {
        assertThatThrownBy(() -> empleadoService.validarEmpleado("usuario", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La contraseña no puede venir nula");

        verifyNoInteractions(empleadoRepository);
    }

    @Test
    void validarEmpleado_conUsernameVacio_delegaEnRepositorio() {
        when(empleadoRepository.findByUsernameAndPassword("", "clave")).thenReturn(Optional.empty());

        boolean resultado = empleadoService.validarEmpleado("", "clave");

        assertThat(resultado).isFalse();
        verify(empleadoRepository).findByUsernameAndPassword("", "clave");
    }

    @Test
    void validarEmpleado_conPasswordVacia_delegaEnRepositorio() {
        when(empleadoRepository.findByUsernameAndPassword("usuario", "")).thenReturn(Optional.empty());

        boolean resultado = empleadoService.validarEmpleado("usuario", "");

        assertThat(resultado).isFalse();
        verify(empleadoRepository).findByUsernameAndPassword("usuario", "");
    }

    @Test
    void traerEmpleadoPorUsername_conEmpleadoExistente_devuelveEmpleado() {
        Empleado empleado = empleado("usuario");
        when(empleadoRepository.findByUsername("usuario")).thenReturn(Optional.of(empleado));

        Empleado resultado = empleadoService.traerEmpleadoPorUsername("usuario");

        assertThat(resultado).isSameAs(empleado);
    }

    @Test
    void traerEmpleadoPorUsername_conUsernameInexistente_lanzaExcepcion() {
        when(empleadoRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empleadoService.traerEmpleadoPorUsername("fantasma"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Empleado no encontrado con username: fantasma");
    }

    @Test
    void traerEmpleadoPorUsername_conUsernameNulo_lanzaExcepcion() {
        assertThatThrownBy(() -> empleadoService.traerEmpleadoPorUsername(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El username no puede venir nulo.");

        verifyNoInteractions(empleadoRepository);
    }

    @Test
    void traerEmpleadoPorUsername_conUsernameVacio_delegaYSiNoExisteLanzaExcepcion() {
        when(empleadoRepository.findByUsername("")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empleadoService.traerEmpleadoPorUsername(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Empleado no encontrado con username: ");
    }

    private Empleado empleado(String username) {
        Empleado empleado = new Empleado();
        empleado.setUsername(username);
        empleado.setNombre("Empleado " + username);
        return empleado;
    }
}
