package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TraeMaterialVinilo;
import com.puntografico.puntografico.repository.TraeMaterialViniloRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesViniloDeCorteService {

    private final TraeMaterialViniloRepository traeMaterialViniloRepository;

    public List<TraeMaterialVinilo>  buscarTodosTraeMaterialVinilo() {
        return traeMaterialViniloRepository.findAll();
    }

    public TraeMaterialVinilo buscarTraeMaterialViniloPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return traeMaterialViniloRepository.findById(id).get();
    }
}
