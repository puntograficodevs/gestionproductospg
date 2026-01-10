package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TipoFazCatalogo;
import com.puntografico.puntografico.domain.TipoLaminadoCatalogo;
import com.puntografico.puntografico.repository.TipoFazCatalogoRepository;
import com.puntografico.puntografico.repository.TipoLaminadoCatalogoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesCatalogoService {

    private final TipoFazCatalogoRepository tipoFazCatalogoRepository;

    private final TipoLaminadoCatalogoRepository tipoLaminadoCatalogoRepository;

    public List<TipoFazCatalogo> buscarTodosTipoFazCatalogo() {
        return tipoFazCatalogoRepository.findAll();
    }

    public List<TipoLaminadoCatalogo> buscarTodosTipoLaminadoCatalogo() {
        return tipoLaminadoCatalogoRepository.findAll();
    }

    public TipoLaminadoCatalogo buscarTipoLaminadoCatalogoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tipoLaminadoCatalogoRepository.findById(id).get();
    }

    public TipoFazCatalogo buscarTipoFazCatalogoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tipoFazCatalogoRepository.findById(id).get();
    }
}
