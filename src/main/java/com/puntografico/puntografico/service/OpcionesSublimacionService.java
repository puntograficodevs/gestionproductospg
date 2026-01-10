package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadSublimacion;
import com.puntografico.puntografico.domain.MaterialSublimacion;
import com.puntografico.puntografico.repository.CantidadSublimacionRepository;
import com.puntografico.puntografico.repository.MaterialSublimacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesSublimacionService {

    private final MaterialSublimacionRepository materialSublimacionRepository;
    private final CantidadSublimacionRepository cantidadSublimacionRepository;

    public List<MaterialSublimacion> buscarTodosMaterialSublimacion() {
        return materialSublimacionRepository.findAll();
    }

    public List<CantidadSublimacion> buscarTodosCantidadSublimacion() {
        return cantidadSublimacionRepository.findAll();
    }

    public MaterialSublimacion buscarMaterialSublimacionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return materialSublimacionRepository.findById(id).get();
    }
    public CantidadSublimacion buscarCantidadSublimacionPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadSublimacionRepository.findById(id).get();
    }
}
