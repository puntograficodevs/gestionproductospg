package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenRifasBonosContribucion;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.RifasBonosContribucion;
import com.puntografico.puntografico.repository.OrdenRifasBonosContribucionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenRifasBonosContribucionService {

    private final OrdenRifasBonosContribucionRepository ordenRifasBonosContribucionRepository;

    public OrdenRifasBonosContribucion guardar(OrdenTrabajo ordenTrabajo, RifasBonosContribucion rifasBonosContribucion, Long idOrdenRifasBonosContribucion) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(rifasBonosContribucion, "Debe venir una rifa o un bono contribución para enlazar.");

        OrdenRifasBonosContribucion ordenRifasBonosContribucion = (idOrdenRifasBonosContribucion != null) ? ordenRifasBonosContribucionRepository.findById(idOrdenRifasBonosContribucion).get() : new OrdenRifasBonosContribucion();
        ordenRifasBonosContribucion.setOrdenTrabajo(ordenTrabajo);
        ordenRifasBonosContribucion.setRifasBonosContribucion(rifasBonosContribucion);
        ordenRifasBonosContribucion.setCantidad(rifasBonosContribucion.getCantidad());

        return ordenRifasBonosContribucionRepository.save(ordenRifasBonosContribucion);
    }

    public OrdenRifasBonosContribucion buscarPorId(Long id) {
        return ordenRifasBonosContribucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenRifasBonosContribucion con ID " + id + " no encontrada"));
    }

    public OrdenRifasBonosContribucion buscarPorOrdenId(Long id) {
        return ordenRifasBonosContribucionRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenRifasBonosContribucionRepository.deleteById(id);
    }
}
