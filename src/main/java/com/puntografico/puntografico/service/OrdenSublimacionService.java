package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenSublimacion;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.Sublimacion;
import com.puntografico.puntografico.repository.OrdenSublimacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenSublimacionService {

    private final OrdenSublimacionRepository ordenSublimacionRepository;

    public OrdenSublimacion guardar(OrdenTrabajo ordenTrabajo, Sublimacion sublimacion, Long idOrdenSublimacion) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(sublimacion, "Debe venir un sublimacion para enlazar.");

        OrdenSublimacion ordenSublimacion = (idOrdenSublimacion != null) ? ordenSublimacionRepository.findById(idOrdenSublimacion).get() : new OrdenSublimacion();
        ordenSublimacion.setCantidad(sublimacion.getCantidad());
        ordenSublimacion.setOrdenTrabajo(ordenTrabajo);
        ordenSublimacion.setSublimacion(sublimacion);

        return ordenSublimacionRepository.save(ordenSublimacion);
    }

    public OrdenSublimacion buscarPorId(Long id) {
        return ordenSublimacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenSublimacion con ID " + id + " no encontrada"));
    }

    public OrdenSublimacion buscarPorOrdenId(Long id) {
        return ordenSublimacionRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenSublimacionRepository.deleteById(id);
    }
}
