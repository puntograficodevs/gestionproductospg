package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Rol;
import com.puntografico.puntografico.repository.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id).get();
    }
}
