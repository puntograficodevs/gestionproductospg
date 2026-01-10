package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Folleto;
import com.puntografico.puntografico.domain.OrdenFolleto;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenFolletoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenFolletoService {

    private final OrdenFolletoRepository ordenFolletoRepository;

    public OrdenFolleto guardar(OrdenTrabajo ordenTrabajo, Folleto folleto, Long idOrdenFolleto) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(folleto, "Debe venir un folleto para enlazar.");

        OrdenFolleto ordenFolleto = (idOrdenFolleto != null) ? ordenFolletoRepository.findById(idOrdenFolleto).get() : new OrdenFolleto();
        ordenFolleto.setCantidad(folleto.getCantidad());
        ordenFolleto.setOrdenTrabajo(ordenTrabajo);
        ordenFolleto.setFolleto(folleto);

        return ordenFolletoRepository.save(ordenFolleto);
    }

    public OrdenFolleto buscarPorId(Long id) {
        return ordenFolletoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenFolleto con ID " + id + " no encontrada"));
    }

    public OrdenFolleto buscarPorOrdenId(Long id) {
        return ordenFolletoRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenFolletoRepository.deleteById(id);
    }
}
