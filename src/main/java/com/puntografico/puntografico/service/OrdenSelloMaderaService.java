package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenSelloMaderaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenSelloMaderaService {

    private final OrdenSelloMaderaRepository ordenSelloMaderaRepository;

    public OrdenSelloMadera guardar(OrdenTrabajo ordenTrabajo, SelloMadera selloMadera, Long idOrdenSelloMadera) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(selloMadera, "Debe venir un sello para enlazar.");

        OrdenSelloMadera ordenSelloMadera = (idOrdenSelloMadera != null) ? ordenSelloMaderaRepository.findById(idOrdenSelloMadera).get() : new OrdenSelloMadera();
        ordenSelloMadera.setSelloMadera(selloMadera);
        ordenSelloMadera.setOrdenTrabajo(ordenTrabajo);
        ordenSelloMadera.setCantidad(selloMadera.getCantidad());

        return ordenSelloMaderaRepository.save(ordenSelloMadera);
    }

    public OrdenSelloMadera buscarPorId(Long id) {
        return ordenSelloMaderaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenSelloMadera con ID " + id + " no encontrada."));
    }

    public OrdenSelloMadera buscarPorOrdenId(Long id) {
        return ordenSelloMaderaRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenSelloMaderaRepository.deleteById(id);
    }
}
