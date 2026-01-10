package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenCierraBolsasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenCierraBolsasService {

    private final OrdenCierraBolsasRepository ordenCierraBolsasRepository;

    public OrdenCierraBolsas guardar(OrdenTrabajo ordenTrabajo, CierraBolsas cierraBolsas, Long idOrdenCierraBolsas) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(cierraBolsas, "Debe venir un cierra bolsas para enlazar");

        OrdenCierraBolsas ordenCierraBolsas = (idOrdenCierraBolsas != null) ? ordenCierraBolsasRepository.findById(idOrdenCierraBolsas).get() : new OrdenCierraBolsas();
        ordenCierraBolsas.setCantidad(cierraBolsas.getCantidad());
        ordenCierraBolsas.setOrdenTrabajo(ordenTrabajo);
        ordenCierraBolsas.setCierraBolsas(cierraBolsas);

        return ordenCierraBolsasRepository.save(ordenCierraBolsas);
    }

    public OrdenCierraBolsas buscarPorId(Long id) {
        return ordenCierraBolsasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenCierraBolsas con ID " + id + " no encontrada"));
    }

    public OrdenCierraBolsas buscarPorOrdenId(Long id) {
        return ordenCierraBolsasRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenCierraBolsasRepository.deleteById(id);
    }
}
