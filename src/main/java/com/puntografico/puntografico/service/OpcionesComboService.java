package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TipoCombo;
import com.puntografico.puntografico.repository.TipoComboRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesComboService {

    private final TipoComboRepository tipoComboRepository;

    public List<TipoCombo> buscarTodosTipoCombo() {
        return tipoComboRepository.findAll();
    }

    public TipoCombo buscarTipoComboPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tipoComboRepository.findById(id).get();
    }
}
