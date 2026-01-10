package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenSobreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenSobreService {

    private final OrdenSobreRepository ordenSobreRepository;

    public OrdenSobre guardar(OrdenTrabajo ordenTrabajo, Sobre sobre, Long idOrdenSobre) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(sobre, "Debe venir un sobre para enlazar.");

        OrdenSobre ordenSobre = (idOrdenSobre != null) ? ordenSobreRepository.findById(idOrdenSobre).get() : new OrdenSobre();
        ordenSobre.setCantidad(sobre.getCantidad());
        ordenSobre.setOrdenTrabajo(ordenTrabajo);
        ordenSobre.setSobre(sobre);

        return ordenSobreRepository.save(ordenSobre);
    }

    public OrdenSobre buscarPorId(Long id) {
        return ordenSobreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenSobre con ID " + id + " no encontrada"));
    }

    public OrdenSobre buscarPorOrdenId(Long id) {
        return ordenSobreRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenSobreRepository.deleteById(id);
    }
}
