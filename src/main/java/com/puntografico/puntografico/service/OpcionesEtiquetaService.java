package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesEtiquetaService {

    private final TipoPapelEtiquetaRepository tipoPapelEtiquetaRepository;
    private final TipoLaminadoEtiquetaRepository tipoLaminadoEtiquetaRepository;
    private final TamanioPerforacionRepository tamanioPerforacionRepository;
    private final TipoFazEtiquetaRepository tipoFazEtiquetaRepository;
    private final CantidadEtiquetaRepository cantidadEtiquetaRepository;
    private final MedidaEtiquetaRepository medidaEtiquetaRepository;

    public List<TipoPapelEtiqueta> buscarTodosTipoPapelEtiqueta() {
        return tipoPapelEtiquetaRepository.findAll();
    }

    public List<TipoLaminadoEtiqueta> buscarTodosTipoLaminadoEtiqueta() {
        return tipoLaminadoEtiquetaRepository.findAll();
    }

    public List<TamanioPerforacion> buscarTodosTamanioPerforacion() {
        return tamanioPerforacionRepository.findAll();
    }

    public List<TipoFazEtiqueta> buscarTodosTipoFazEtiqueta() {
        return tipoFazEtiquetaRepository.findAll();
    }

    public List<CantidadEtiqueta> buscarTodosCantidadEtiqueta() {
        return cantidadEtiquetaRepository.findAll();
    }

    public List<MedidaEtiqueta> buscarTodosMedidaEtiqueta() {
        return medidaEtiquetaRepository.findAll();
    }

    public TipoPapelEtiqueta buscarTipoPapelEtiquetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelEtiquetaRepository.findById(id).get();
    }

    public TipoLaminadoEtiqueta buscarTipoLaminadoEtiquetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoLaminadoEtiquetaRepository.findById(id).get();
    }

    public TamanioPerforacion buscarTamanioPerforacionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tamanioPerforacionRepository.findById(id).get();
    }

    public TipoFazEtiqueta buscarTipoFazEtiquetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoFazEtiquetaRepository.findById(id).get();
    }

    public CantidadEtiqueta buscarCantidadEtiquetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadEtiquetaRepository.findById(id).get();
    }

    public MedidaEtiqueta buscarMedidaEtiquetaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaEtiquetaRepository.findById(id).get();
    }
}
