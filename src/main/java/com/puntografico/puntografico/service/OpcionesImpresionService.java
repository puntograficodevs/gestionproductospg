package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesImpresionService {

    private final TipoColorImpresionRepository tipoColorImpresionRepository;
    private final TamanioHojaImpresionRepository tamanioHojaImpresionRepository;
    private final TipoFazImpresionRepository tipoFazImpresionRepository;
    private final TipoPapelImpresionRepository tipoPapelImpresionRepository;
    private final CantidadImpresionRepository cantidadImpresionRepository;
    private final TipoImpresionRepository tipoImpresionRepository;

    public List<TipoColorImpresion> buscarTodosTipoColorImpresion() {
        return tipoColorImpresionRepository.findAll();
    }

    public List<TamanioHojaImpresion> buscarTodosTamanioHojaImpresion() {
        return tamanioHojaImpresionRepository.findAll();
    }

    public List<TipoFazImpresion> buscarTodosTipoFazImpresion() {
        return tipoFazImpresionRepository.findAll();
    }

    public List<TipoPapelImpresion> buscarTodosTipoPapelImpresion() {
        return tipoPapelImpresionRepository.findAll();
    }

    public List<CantidadImpresion> buscarTodosCantidadImpresion() {
        return cantidadImpresionRepository.findAll();
    }

    public List<TipoImpresion> buscarTodosTipoImpresion() {
        return tipoImpresionRepository.findAll();
    }

    public TipoColorImpresion buscarTipoColorImpresionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorImpresionRepository.findById(id).get();
    }
    public TamanioHojaImpresion buscarTamanioHojaImpresionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tamanioHojaImpresionRepository.findById(id).get();
    }
    public TipoFazImpresion buscarTipoFazImpresionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoFazImpresionRepository.findById(id).get();
    }
    public TipoPapelImpresion buscarTipoPapelImpresionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelImpresionRepository.findById(id).get();
    }
    public CantidadImpresion buscarCantidadImpresionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadImpresionRepository.findById(id).get();
    }
    public TipoImpresion buscarTipoImpresionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoImpresionRepository.findById(id).get();
    }
}
