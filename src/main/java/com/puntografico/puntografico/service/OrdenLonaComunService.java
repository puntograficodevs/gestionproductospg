package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.LonaComun;
import com.puntografico.puntografico.domain.OrdenLonaComun;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenLonaComunRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenLonaComunService {

    private final OrdenLonaComunRepository ordenLonaComunRepository;

    public OrdenLonaComun guardar(OrdenTrabajo ordenTrabajo, LonaComun lonaComun, Long idOrdenLonaComun) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(lonaComun, "Debe venir una lona común para enlazar.");

        OrdenLonaComun ordenLonaComun = (idOrdenLonaComun != null) ? ordenLonaComunRepository.findById(idOrdenLonaComun).get() : new OrdenLonaComun();
        ordenLonaComun.setLonaComun(lonaComun);
        ordenLonaComun.setOrdenTrabajo(ordenTrabajo);
        ordenLonaComun.setCantidad(lonaComun.getCantidad());

        return ordenLonaComunRepository.save(ordenLonaComun);
    }

    public OrdenLonaComun buscarPorId(Long id) {
        return ordenLonaComunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenLonaComun con ID " + id + " no encontrada."));
    }

    public OrdenLonaComun buscarPorOrdenId(Long id) {
        return ordenLonaComunRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenLonaComunRepository.deleteById(id);
    }
}
