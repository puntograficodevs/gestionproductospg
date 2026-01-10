package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadTurnero;
import com.puntografico.puntografico.domain.MedidaTurnero;
import com.puntografico.puntografico.domain.TipoColorTurnero;
import com.puntografico.puntografico.repository.CantidadTurneroRepository;
import com.puntografico.puntografico.repository.MedidaTurneroRepository;
import com.puntografico.puntografico.repository.TipoColorTurneroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesTurneroService {

    private final TipoColorTurneroRepository tipoColorTurneroRepository;
    private final CantidadTurneroRepository cantidadTurneroRepository;
    private final MedidaTurneroRepository medidaTurneroRepository;

    public List<TipoColorTurnero> buscarTodosTipoColorTurnero() {
        return tipoColorTurneroRepository.findAll();
    }

    public List<CantidadTurnero> buscarTodosCantidadTurnero() {
        return cantidadTurneroRepository.findAll();
    }

    public List<MedidaTurnero> buscarTodosMedidaTurnero() {
        return medidaTurneroRepository.findAll();
    }

    public TipoColorTurnero buscarTipoColorTurneroPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorTurneroRepository.findById(id).get();
    }
    public CantidadTurnero buscarCantidadTurneroPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadTurneroRepository.findById(id).get();
    }
    public MedidaTurnero buscarMedidaTurneroPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaTurneroRepository.findById(id).get();
    }
}
