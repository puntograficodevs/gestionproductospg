package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.MedioPago;
import com.puntografico.puntografico.repository.MedioPagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional @AllArgsConstructor
public class MedioPagoService {

    private final MedioPagoRepository medioPagoRepository;

    public List<MedioPago> buscarTodos() {
        return medioPagoRepository.findAll();
    }
}
