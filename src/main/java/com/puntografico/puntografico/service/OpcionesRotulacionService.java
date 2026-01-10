package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TipoCorteRotulacion;
import com.puntografico.puntografico.domain.TipoRotulacion;
import com.puntografico.puntografico.repository.TipoCorteRotulacionRepository;
import com.puntografico.puntografico.repository.TipoRotulacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesRotulacionService {

    private final TipoRotulacionRepository tipoRotulacionRepository;
    private final TipoCorteRotulacionRepository tipoCorteRotulacionRepository;

    public List<TipoRotulacion> buscarTodosTipoRotulacion() {
        return tipoRotulacionRepository.findAll();
    }

    public List<TipoCorteRotulacion> buscarTodosTipoCorteRotulacion() {
        return tipoCorteRotulacionRepository.findAll();
    }

    public TipoRotulacion buscarTipoRotulacionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tipoRotulacionRepository.findById(id).get();
    }

    public TipoCorteRotulacion buscarTipoCorteRotulacionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return tipoCorteRotulacionRepository.findById(id).get();
    }
}
