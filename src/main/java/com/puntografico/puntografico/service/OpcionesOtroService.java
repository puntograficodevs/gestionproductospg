package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TipoColorOtro;
import com.puntografico.puntografico.repository.TipoColorOtroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesOtroService {

    private final TipoColorOtroRepository tipoColorOtroRepository;

    public List<TipoColorOtro> buscarTodosTipoColorOtro() {
        return tipoColorOtroRepository.findAll();
    }

    public TipoColorOtro buscarTipoColorOtroPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tipoColorOtroRepository.findById(id).get();
    }
}
