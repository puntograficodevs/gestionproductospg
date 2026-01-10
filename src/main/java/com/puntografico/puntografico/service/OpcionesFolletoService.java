package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesFolletoService {

    private final TipoPapelFolletoRepository tipoPapelFolletoRepository;
    private final TipoColorFolletoRepository tipoColorFolletoRepository;
    private final TipoFazFolletoRepository tipoFazFolletoRepository;
    private final TamanioHojaFolletoRepository tamanioHojaFolletoRepository;
    private final TipoFolletoRepository tipoFolletoRepository;
    private final CantidadFolletoRepository cantidadFolletoRepository;

    public List<TipoPapelFolleto> buscarTodosTipoPapelFolleto() {
        return tipoPapelFolletoRepository.findAll();
    }

    public List<TipoColorFolleto> buscarTodosTipoColorFolleto() {
        return tipoColorFolletoRepository.findAll();
    }

    public List<TipoFazFolleto> buscarTodosTipoFazFolleto() {
        return tipoFazFolletoRepository.findAll();
    }

    public List<TamanioHojaFolleto> buscarTodosTamanioHojaFolleto() {
        return tamanioHojaFolletoRepository.findAll();
    }

    public List<TipoFolleto> buscarTodosTipoFolleto() {
        return tipoFolletoRepository.findAll();
    }

    public List<CantidadFolleto> buscarTodosCantidadFolleto() {
        return cantidadFolletoRepository.findAll();
    }

    public TipoPapelFolleto buscarTipoPapelFolletoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelFolletoRepository.findById(id).get();
    }
    public TipoColorFolleto buscarTipoColorFolletoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorFolletoRepository.findById(id).get();
    }
    public TipoFazFolleto buscarTipoFazFolletoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoFazFolletoRepository.findById(id).get();
    }
    public TamanioHojaFolleto buscarTamanioHojaFolletoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tamanioHojaFolletoRepository.findById(id).get();
    }
    public TipoFolleto buscarTipoFolletoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoFolletoRepository.findById(id).get();
    }
    public CantidadFolleto buscarCantidadFolletoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadFolletoRepository.findById(id).get();
    }
}
