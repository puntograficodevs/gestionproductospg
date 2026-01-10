package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesTalonarioService {

    private final TipoTalonarioRepository tipoTalonarioRepository;
    private final TipoTroqueladoTalonarioRepository tipoTroqueladoTalonarioRepository;
    private final ModoTalonarioRepository modoTalonarioRepository;
    private final TipoColorTalonarioRepository tipoColorTalonarioRepository;
    private final MedidaTalonarioRepository medidaTalonarioRepository;
    private final TipoPapelTalonarioRepository tipoPapelTalonarioRepository;
    private final CantidadTalonarioRepository cantidadTalonarioRepository;

    public List<TipoTalonario> buscarTodosTipoTalonario() {
        return tipoTalonarioRepository.findAll();
    }

    public List<TipoTroqueladoTalonario> buscarTodosTipoTroqueladoTalonario() {
        return tipoTroqueladoTalonarioRepository.findAll();
    }

    public List<ModoTalonario> buscarTodosModoTalonario() {
        return modoTalonarioRepository.findAll();
    }

    public List<TipoColorTalonario> buscarTodosTipoColorTalonario() {
        return tipoColorTalonarioRepository.findAll();
    }

    public List<MedidaTalonario> buscarTodosMedidaTalonario() {
        return medidaTalonarioRepository.findAll();
    }

    public List<TipoPapelTalonario> buscarTodosTipoPapelTalonario() {
        return tipoPapelTalonarioRepository.findAll();
    }

    public List<CantidadTalonario> buscarTodosCantidadTalonario() {
        return cantidadTalonarioRepository.findAll();
    }

    public TipoTalonario buscarTipoTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoTalonarioRepository.findById(id).get();
    }
    public TipoTroqueladoTalonario buscarTipoTroqueladoTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoTroqueladoTalonarioRepository.findById(id).get();
    }
    public ModoTalonario buscarModoTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return modoTalonarioRepository.findById(id).get();
    }
    public TipoColorTalonario buscarTipoColorTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorTalonarioRepository.findById(id).get();
    }
    public MedidaTalonario buscarMedidaTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaTalonarioRepository.findById(id).get();
    }
    public TipoPapelTalonario buscarTipoPapelTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelTalonarioRepository.findById(id).get();
    }
    public CantidadTalonario buscarCantidadTalonarioPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadTalonarioRepository.findById(id).get();
    }
}
