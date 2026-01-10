package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.ModeloSelloAutomatico;
import com.puntografico.puntografico.repository.ModeloSelloAutomaticoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesSelloAutomaticoService {
    private final ModeloSelloAutomaticoRepository modeloSelloAutomaticoRepository;

    public List<ModeloSelloAutomatico> buscarTodosModeloSelloAutomatico() {
        return modeloSelloAutomaticoRepository.findAll();
    }

    public ModeloSelloAutomatico buscarModeloSelloAutomaticoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return modeloSelloAutomaticoRepository.findById(id).get();
    }
}
