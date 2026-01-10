package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.LonaPublicitaria;
import com.puntografico.puntografico.domain.OrdenLonaPublicitaria;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenLonaPublicitariaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenLonaPublicitariaService {

    private final OrdenLonaPublicitariaRepository ordenLonaPublicitariaRepository;

    public OrdenLonaPublicitaria guardar(OrdenTrabajo ordenTrabajo, LonaPublicitaria lonaPublicitaria, Long idOrdenLonaPublicitaria) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(lonaPublicitaria, "Debe venir una lona publicitaria para enlazar.");

        OrdenLonaPublicitaria ordenLonaPublicitaria = (idOrdenLonaPublicitaria != null) ? ordenLonaPublicitariaRepository.findById(idOrdenLonaPublicitaria).get() : new OrdenLonaPublicitaria();
        ordenLonaPublicitaria.setLonaPublicitaria(lonaPublicitaria);
        ordenLonaPublicitaria.setOrdenTrabajo(ordenTrabajo);
        ordenLonaPublicitaria.setCantidad(lonaPublicitaria.getCantidad());

        return ordenLonaPublicitariaRepository.save(ordenLonaPublicitaria);
    }

    public OrdenLonaPublicitaria buscarPorId(Long id) {
        return ordenLonaPublicitariaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenLonaPublicitaria con ID " + id + " no encontrada."));
    }

    public OrdenLonaPublicitaria buscarPorOrdenId(Long id) {
        return ordenLonaPublicitariaRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenLonaPublicitariaRepository.deleteById(id);
    }
}
