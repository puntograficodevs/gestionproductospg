package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Etiqueta;
import com.puntografico.puntografico.domain.OrdenEtiqueta;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenEtiquetaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenEtiquetaService {

    private final OrdenEtiquetaRepository ordenEtiquetaRepository;

    public OrdenEtiqueta guardar(OrdenTrabajo ordenTrabajo, Etiqueta etiqueta, Long idOrdenEtiqueta) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(etiqueta, "Debe venir una etiqueta para enlazar.");

        OrdenEtiqueta ordenEtiqueta = (idOrdenEtiqueta != null) ? ordenEtiquetaRepository.findById(idOrdenEtiqueta).get() : new OrdenEtiqueta();
        ordenEtiqueta.setCantidad(etiqueta.getCantidad());
        ordenEtiqueta.setOrdenTrabajo(ordenTrabajo);
        ordenEtiqueta.setEtiqueta(etiqueta);

        return ordenEtiquetaRepository.save(ordenEtiqueta);
    }

    public OrdenEtiqueta buscarPorId(Long id) {
        return ordenEtiquetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenEtiqueta con ID " + id + " no encontrada"));
    }

    public OrdenEtiqueta buscarPorOrdenId(Long id) {
        return ordenEtiquetaRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenEtiquetaRepository.deleteById(id);
    }
}
