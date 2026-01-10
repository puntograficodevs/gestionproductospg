package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesViniloService {

    private final TipoViniloRepository tipoViniloRepository;
    private final TipoAdicionalViniloRepository tipoAdicionalViniloRepository;
    private final TipoCorteViniloRepository tipoCorteViniloRepository;
    private final MedidaViniloRepository medidaViniloRepository;
    private final CantidadViniloRepository cantidadViniloRepository;

    public List<TipoVinilo> buscarTodosTipoVinilo() {
        return tipoViniloRepository.findAll();
    }

    public List<TipoAdicionalVinilo> buscarTodosTipoAdicionalVinilo() {
        return tipoAdicionalViniloRepository.findAll();
    }

    public List<TipoCorteVinilo> buscarTodosTipoCorteVinilo() {
        return tipoCorteViniloRepository.findAll();
    }

    public List<MedidaVinilo> buscarTodosMedidaVinilo() {
        return medidaViniloRepository.findAll();
    }

    public List<CantidadVinilo> buscarTodosCantidadVinilo() {
        return cantidadViniloRepository.findAll();
    }

    public TipoVinilo buscarTipoViniloPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoViniloRepository.findById(id).get();
    }
    public TipoAdicionalVinilo buscarTipoAdicionalViniloPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoAdicionalViniloRepository.findById(id).get();
    }
    public TipoCorteVinilo buscarTipoCorteViniloPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoCorteViniloRepository.findById(id).get();
    }
    public MedidaVinilo buscarMedidaViniloPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaViniloRepository.findById(id).get();
    }
    public CantidadVinilo buscarCantidadViniloPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadViniloRepository.findById(id).get();
    }

}
