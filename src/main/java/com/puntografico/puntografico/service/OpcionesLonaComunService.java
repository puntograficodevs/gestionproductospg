package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.MedidaLonaComun;
import com.puntografico.puntografico.domain.TipoLonaComun;
import com.puntografico.puntografico.repository.MedidaLonaComunRepository;
import com.puntografico.puntografico.repository.TipoLonaComunRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesLonaComunService {

    private final TipoLonaComunRepository tipoLonaComunRepository;
    private final MedidaLonaComunRepository medidaLonaComunRepository;

    public List<TipoLonaComun> buscarTodosTipoLonaComun() {
        return tipoLonaComunRepository.findAll();
    }

    public List<MedidaLonaComun> buscarTodosMedidaLonaComun() {
        return medidaLonaComunRepository.findAll();
    }

    public TipoLonaComun buscarTipoLonaComunPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoLonaComunRepository.findById(id).get();
    }

    public MedidaLonaComun buscarMedidaLonaComunPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaLonaComunRepository.findById(id).get();
    }
}
