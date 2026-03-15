package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.repository.EmpleadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public boolean validarEmpleado(String username, String password) {
        Assert.notNull(username, "El username no puede venir nulo.");
        Assert.notNull(password, "La contraseña no puede venir nula");

        Optional<Empleado> empleado = empleadoRepository.findByUsernameAndPassword(username, password);
        return empleado.isPresent();
    }

    public Empleado traerEmpleadoPorUsername(String username) {
        Assert.notNull(username, "El username no puede venir nulo.");
        return empleadoRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con username: " + username));
    }
}
