package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadHojasMembreteadas;
import com.puntografico.puntografico.domain.MedidaHojasMembreteadas;
import com.puntografico.puntografico.domain.TipoColorHojasMembreteadas;
import com.puntografico.puntografico.repository.CantidadHojasMembreteadasRepository;
import com.puntografico.puntografico.repository.MedidaHojasMembreteadasRepository;
import com.puntografico.puntografico.repository.TipoColorHojasMembreteadasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesHojasMembreteadasService {

    private final MedidaHojasMembreteadasRepository medidaHojasMembreteadasRepository;
    private final TipoColorHojasMembreteadasRepository tipoColorHojasMembreteadasRepository;
    private final CantidadHojasMembreteadasRepository cantidadHojasMembreteadasRepository;

    public List<MedidaHojasMembreteadas> buscarTodosMedidaHojasMembreteadas() {
        return medidaHojasMembreteadasRepository.findAll();
    }

    public List<TipoColorHojasMembreteadas> buscarTodosTipoColorHojasMembreteadas() {
        return tipoColorHojasMembreteadasRepository.findAll();
    }

    public List<CantidadHojasMembreteadas> buscarTodosCantidadHojasMembreteadas() {
        return cantidadHojasMembreteadasRepository.findAll();
    }

    public MedidaHojasMembreteadas buscarMedidaHojasMembreteadasPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaHojasMembreteadasRepository.findById(id).get();
    }
    public TipoColorHojasMembreteadas buscarTipoColorHojasMembreteadasPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoColorHojasMembreteadasRepository.findById(id).get();
    }
    public CantidadHojasMembreteadas buscarCantidadHojasMembreteadasPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadHojasMembreteadasRepository.findById(id).get();
    }

}
