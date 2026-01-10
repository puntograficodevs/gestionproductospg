package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TamanioSelloMadera;
import com.puntografico.puntografico.repository.TamanioSelloMaderaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesSelloMaderaService {

    private final TamanioSelloMaderaRepository tamanioSelloMaderaRepository;

    public List<TamanioSelloMadera> buscarTodosTamanioSelloMadera() {
        return tamanioSelloMaderaRepository.findAll();
    }

    public TamanioSelloMadera buscarTamanioSelloMaderaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tamanioSelloMaderaRepository.findById(id).get();
    }
}
