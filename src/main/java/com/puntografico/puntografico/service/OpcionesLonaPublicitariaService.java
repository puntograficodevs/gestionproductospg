package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.MedidaLonaPublicitaria;
import com.puntografico.puntografico.domain.TipoLonaPublicitaria;
import com.puntografico.puntografico.repository.MedidaLonaPublicitariaRepository;
import com.puntografico.puntografico.repository.TipoLonaPublicitariaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesLonaPublicitariaService {
    private final TipoLonaPublicitariaRepository tipoLonaPublicitariaRepository;
    private final MedidaLonaPublicitariaRepository medidaLonaPublicitariaRepository;

    public List<TipoLonaPublicitaria> buscarTodosTipoLonaPublicitaria() {
        return tipoLonaPublicitariaRepository.findAll();
    }

    public List<MedidaLonaPublicitaria> buscarTodosMedidaLonaPublicitaria() {
        return medidaLonaPublicitariaRepository.findAll();
    }

    public TipoLonaPublicitaria buscarTipoLonaPublicitariaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoLonaPublicitariaRepository.findById(id).get();
    }

    public MedidaLonaPublicitaria buscarMedidaLonaPublicitariaPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaLonaPublicitariaRepository.findById(id).get();
    }
}
