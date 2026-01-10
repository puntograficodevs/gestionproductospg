package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.MedidaViniloPlasticoCorrugado;
import com.puntografico.puntografico.repository.MedidaViniloPlasticoCorrugadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesViniloPlasticoCorrugadoService {

    private final MedidaViniloPlasticoCorrugadoRepository medidaViniloPlasticoCorrugadoRepository;

    public List<MedidaViniloPlasticoCorrugado> buscarTodosMedidaViniloPlasticoCorrugado() {
        return medidaViniloPlasticoCorrugadoRepository.findAll();
    }

    public MedidaViniloPlasticoCorrugado buscarMedidaViniloPlasticoCorrugadoPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");

        return medidaViniloPlasticoCorrugadoRepository.findById(id).get();
    }
}
