package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenTarjetaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenTarjetaService {

    private final OrdenTarjetaRepository ordenTarjetaRepository;

    public OrdenTarjeta guardar(OrdenTrabajo ordenTrabajo, Tarjeta tarjeta, Long idOrdenTarjeta) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(tarjeta, "Debe venir una tarjeta para enlazar.");

        OrdenTarjeta ordenTarjeta = (idOrdenTarjeta != null) ? ordenTarjetaRepository.findById(idOrdenTarjeta).get() : new OrdenTarjeta();
        ordenTarjeta.setCantidad(tarjeta.getCantidad());
        ordenTarjeta.setOrdenTrabajo(ordenTrabajo);
        ordenTarjeta.setTarjeta(tarjeta);

        return ordenTarjetaRepository.save(ordenTarjeta);
    }

    public OrdenTarjeta buscarPorId(Long id) {
        return ordenTarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenTarjeta con ID " + id + " no encontrada"));
    }

    public OrdenTarjeta buscarPorOrdenId(Long id) {
        return ordenTarjetaRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenTarjetaRepository.deleteById(id);
    }
}
