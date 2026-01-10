package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenTurneroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenTurneroService {

    private final OrdenTurneroRepository ordenTurneroRepository;

    public OrdenTurnero guardar(OrdenTrabajo ordenTrabajo, Turnero turnero, Long idOrdenTurnero) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(turnero, "Debe venir un turnero para enlazar.");

        OrdenTurnero ordenTurnero = (idOrdenTurnero != null) ? ordenTurneroRepository.findById(idOrdenTurnero).get() : new OrdenTurnero();
        ordenTurnero.setCantidad(turnero.getCantidad());
        ordenTurnero.setOrdenTrabajo(ordenTrabajo);
        ordenTurnero.setTurnero(turnero);

        return ordenTurneroRepository.save(ordenTurnero);
    }

    public OrdenTurnero buscarPorId(Long id) {
        return ordenTurneroRepository.findById(id).orElseThrow(() -> new RuntimeException("OrdenTurnero con ID " + id + " no encontrada."));
    }

    public OrdenTurnero buscarPorOrdenId(Long id) {
        return ordenTurneroRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenTurneroRepository.deleteById(id);
    }
}
