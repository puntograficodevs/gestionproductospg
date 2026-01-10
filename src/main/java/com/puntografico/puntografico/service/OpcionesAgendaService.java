package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TipoColorAgenda;
import com.puntografico.puntografico.domain.TipoTapaAgenda;
import com.puntografico.puntografico.repository.TipoColorAgendaRepository;
import com.puntografico.puntografico.repository.TipoTapaAgendaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional @AllArgsConstructor
public class OpcionesAgendaService {

    private final TipoTapaAgendaRepository tipoTapaAgendaRepository;

    private final TipoColorAgendaRepository tipoColorAgendaRepository;

    public List<TipoTapaAgenda> buscarTodosTipoTapaAgenda() {
        return tipoTapaAgendaRepository.findAll();
    }

    public List<TipoColorAgenda> buscarTodosTipoColorAgenda() {
        return tipoColorAgendaRepository.findAll();
    }

    public TipoTapaAgenda buscarTipoTapaAgendaPorId(Long id) {
        Assert.notNull(id, "El id del tipo de tapa elegido no puede ser nulo.");
        return tipoTapaAgendaRepository.findById(id).get();
    }

    public TipoColorAgenda buscarTipoColorAgendaPorId(Long id) {
        Assert.notNull(id, "El id del tipo de color elegido no puede ser nulo.");
        return tipoColorAgendaRepository.findById(id).get();
    }
}
