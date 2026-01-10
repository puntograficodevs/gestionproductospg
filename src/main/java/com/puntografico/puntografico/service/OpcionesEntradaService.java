package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesEntradaService {

    private final TipoPapelEntradaRepository tipoPapelEntradaRepository;
    private final TipoColorEntradaRepository tipoColorEntradaRepository;
    private final TipoTroqueladoEntradaRepository tipoTroqueladoEntradaRepository;
    private final MedidaEntradaRepository medidaEntradaRepository;
    private final CantidadEntradaRepository cantidadEntradaRepository;
    private final NumeradoEntradaRepository numeradoEntradaRepository;
    private final TerminacionEntradaRepository terminacionEntradaRepository;

    public List<TipoPapelEntrada> buscarTodosTipoPapelEntrada() {
        return tipoPapelEntradaRepository.findAll();
    }

    public List<TipoColorEntrada> buscarTodosTipoColorEntrada() {
        return tipoColorEntradaRepository.findAll();
    }

    public List<TipoTroqueladoEntrada> buscarTodosTipoTroqueladoEntrada() {
        return tipoTroqueladoEntradaRepository.findAll();
    }

    public List<MedidaEntrada> buscarTodosMedidaEntrada() {
        return medidaEntradaRepository.findAll();
    }

    public List<CantidadEntrada> buscarTodosCantidadEntrada() {
        return cantidadEntradaRepository.findAll();
    }

    public List<NumeradoEntrada> buscarTodosNumeradoEntrada() {
        return numeradoEntradaRepository.findAll();
    }

    public List<TerminacionEntrada> buscarTodosTerminacionEntrada() {
        return terminacionEntradaRepository.findAll();
    }

    public TipoPapelEntrada buscarTipoPapelEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelEntradaRepository.findById(id).get();
    }

    public TipoColorEntrada buscarTipoColorEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorEntradaRepository.findById(id).get();
    }

    public TipoTroqueladoEntrada buscarTipoTroqueladoEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoTroqueladoEntradaRepository.findById(id).get();
    }

    public MedidaEntrada buscarMedidaEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaEntradaRepository.findById(id).get();
    }

    public CantidadEntrada buscarCantidadEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadEntradaRepository.findById(id).get();
    }

    public NumeradoEntrada buscarNumeradoEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return numeradoEntradaRepository.findById(id).get();
    }

    public TerminacionEntrada buscarTerminacionEntradaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return terminacionEntradaRepository.findById(id).get();
    }
}
