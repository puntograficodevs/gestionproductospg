package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Flybanner;
import com.puntografico.puntografico.domain.OrdenFlybanner;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenFlybannerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class OrdenFlybannerService {

    private final OrdenFlybannerRepository ordenFlybannerRepository;

    public OrdenFlybanner guardar(OrdenTrabajo ordenTrabajo, Flybanner flybanner, Long idOrdenFlybanner) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden para enlazar.");
        Assert.notNull(flybanner, "Debe venir un flybanner para enlazar.");

        OrdenFlybanner ordenFlybanner = (idOrdenFlybanner != null) ? ordenFlybannerRepository.findById(idOrdenFlybanner).get() : new OrdenFlybanner();
        ordenFlybanner.setCantidad(flybanner.getCantidad());
        ordenFlybanner.setOrdenTrabajo(ordenTrabajo);
        ordenFlybanner.setFlybanner(flybanner);

        return ordenFlybannerRepository.save(ordenFlybanner);
    }

    public OrdenFlybanner buscarPorId(Long id) {
        return ordenFlybannerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenFlybanner con ID " + id + " no encontrada"));
    }

    public OrdenFlybanner buscarPorOrdenId(Long id) {
        return ordenFlybannerRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenFlybannerRepository.deleteById(id);
    }
}
