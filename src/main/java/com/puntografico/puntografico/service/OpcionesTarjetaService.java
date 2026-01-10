package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesTarjetaService {

    private final TipoPapelTarjetaRepository tipoPapelTarjetaRepository;
    private final TipoColorTarjetaRepository tipoColorTarjetaRepository;
    private final TipoFazTarjetaRepository tipoFazTarjetaRepository;
    private final TipoLaminadoTarjetaRepository tipoLaminadoTarjetaRepository;
    private final MedidaTarjetaRepository medidaTarjetaRepository;
    private final CantidadTarjetaRepository cantidadTarjetaRepository;

    public List<TipoPapelTarjeta> buscarTodosTipoPapelTarjeta() {
        return tipoPapelTarjetaRepository.findAll();
    }

    public List<TipoColorTarjeta> buscarTodosTipoColorTarjeta() {
        return tipoColorTarjetaRepository.findAll();
    }

    public List<TipoFazTarjeta> buscarTodosTipoFazTarjeta() {
        return tipoFazTarjetaRepository.findAll();
    }

    public List<TipoLaminadoTarjeta> buscarTodosTipoLaminadoTarjeta() {
        return tipoLaminadoTarjetaRepository.findAll();
    }

    public List<MedidaTarjeta> buscarTodosMedidaTarjeta() {
        return medidaTarjetaRepository.findAll();
    }

    public List<CantidadTarjeta> buscarTodosCantidadTarjeta() {
        return cantidadTarjetaRepository.findAll();
    }

    public TipoPapelTarjeta buscarTipoPapelTarjetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelTarjetaRepository.findById(id).get();
    }
    public TipoColorTarjeta buscarTipoColorTarjetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorTarjetaRepository.findById(id).get();
    }
    public TipoFazTarjeta buscarTipoFazTarjetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoFazTarjetaRepository.findById(id).get();
    }
    public TipoLaminadoTarjeta buscarTipoLaminadoTarjetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoLaminadoTarjetaRepository.findById(id).get();
    }
    public MedidaTarjeta buscarMedidaTarjetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaTarjetaRepository.findById(id).get();
    }
    public CantidadTarjeta buscarCantidadTarjetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadTarjetaRepository.findById(id).get();
    }
}
