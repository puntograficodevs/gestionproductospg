package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.OrdenViniloDeCorte;
import com.puntografico.puntografico.domain.ViniloDeCorte;
import com.puntografico.puntografico.repository.OrdenViniloDeCorteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenViniloDeCorteService {

    private final OrdenViniloDeCorteRepository ordenViniloDeCorteRepository;

    public OrdenViniloDeCorte guardar(OrdenTrabajo ordenTrabajo, ViniloDeCorte viniloDeCorte, Long idOrdenViniloDeCorte) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(viniloDeCorte, "Debe venir un vinilo de corte para enlazar.");

        OrdenViniloDeCorte ordenViniloDeCorte = (idOrdenViniloDeCorte != null) ? ordenViniloDeCorteRepository.findById(idOrdenViniloDeCorte).get() : new OrdenViniloDeCorte();
        ordenViniloDeCorte.setCantidad(viniloDeCorte.getCantidad());
        ordenViniloDeCorte.setOrdenTrabajo(ordenTrabajo);
        ordenViniloDeCorte.setViniloDeCorte(viniloDeCorte);

        return ordenViniloDeCorteRepository.save(ordenViniloDeCorte);
    }

    public OrdenViniloDeCorte buscarPorId(Long id) {
        return ordenViniloDeCorteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenViniloDeCorte con ID " + id + " no encontrada"));
    }

    public OrdenViniloDeCorte buscarPorOrdenId(Long id) {
        return ordenViniloDeCorteRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenViniloDeCorteRepository.deleteById(id);
    }
}
