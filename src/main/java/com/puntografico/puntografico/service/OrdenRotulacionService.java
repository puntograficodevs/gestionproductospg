package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenRotulacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenRotulacionService {

    private final OrdenRotulacionRepository ordenRotulacionRepository;

    public OrdenRotulacion guardar(OrdenTrabajo ordenTrabajo, Rotulacion rotulacion, Long idOrdenRotulacion) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(rotulacion, "Debe venir una rotulacion para enlazar.");

        OrdenRotulacion ordenRotulacion = (idOrdenRotulacion != null) ? ordenRotulacionRepository.findById(idOrdenRotulacion).get() : new OrdenRotulacion();
        ordenRotulacion.setRotulacion(rotulacion);
        ordenRotulacion.setOrdenTrabajo(ordenTrabajo);
        ordenRotulacion.setCantidad(rotulacion.getCantidad());

        return ordenRotulacionRepository.save(ordenRotulacion);
    }

    public OrdenRotulacion buscarPorId(Long id) {
        return ordenRotulacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenRotulacion con ID " + id + " no encontrada."));
    }

    public OrdenRotulacion buscarPorOrdenId(Long id) {
        return ordenRotulacionRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenRotulacionRepository.deleteById(id);
    }
}
