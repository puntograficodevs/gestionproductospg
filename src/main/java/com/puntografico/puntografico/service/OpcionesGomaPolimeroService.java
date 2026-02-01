package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.ModeloGomaPolimero;
import com.puntografico.puntografico.domain.ModeloSelloAutomatico;
import com.puntografico.puntografico.repository.ModeloGomaPolimeroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OpcionesGomaPolimeroService {
    private final ModeloGomaPolimeroRepository modeloGomaPolimeroRepository;

    public List<ModeloGomaPolimero> buscarTodosModeloGomaPolimero() {
        return modeloGomaPolimeroRepository.findAll();
    }

    public ModeloGomaPolimero buscarModeloGomaPolimeroPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return modeloGomaPolimeroRepository.findById(id).get();
    }
}
